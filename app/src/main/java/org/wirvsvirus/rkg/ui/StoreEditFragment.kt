package org.wirvsvirus.rkg.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_store_edit.*
import org.wirvsvirus.rkg.*
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.model.CreateStoreRequestModel
import org.wirvsvirus.rkg.model.OpeningHour
import org.wirvsvirus.rkg.model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreEditFragment : Fragment() {

    private var startDate: String? = null
    private var endDate: String? = null
    private lateinit var loadedStore: Store
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var fromRegistration = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_store_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

        fromRegistration = arguments?.getBoolean(KEY_FROM_REGISTRATION, false) ?: false

        loadedStore = (arguments?.getSerializable(StoreFragment.KEY_STORE_OBJECT) as? Store)
            ?: Store(null, "", "", 0.0, 0.0, "", emptyList(), emptyList(), true)

        loadStoreDataToView()

        storePickOpeningStartButton.setOnClickListener {
            val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                startDate = String.format("%02d:%02d Uhr", hourOfDay, minute)
                storePickOpeningStartLabel.text = startDate
            }
            TimePickerDialog(activity, listener, 6, 0, DateFormat.is24HourFormat(activity)).show()
        }

        storePickOpeningEndButton.setOnClickListener {
            val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endDate = String.format("%02d:%02d Uhr", hourOfDay, minute)
                storePickOpeningEndLabel.text = endDate
            }
            TimePickerDialog(activity, listener, 18, 0, DateFormat.is24HourFormat(activity)).show()
        }

        storeEditFindLocation.setOnClickListener {
            if (!isLocationPermissionGranted()) {
                requestLocationPermission()
            } else {
                retrieveCurrentLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_store_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuStoreEditSave -> saveData()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadStoreDataToView() {
        loadedStore?.let { store ->
            if (store.latitude != -1.0 && store.longitude != -1.0)
                storeEditLocation.text =
                    getString(R.string.locationCoordinateTemplate, store.latitude, store.longitude)
            loadOpeningHoursToView(store.openingHours)
            storeEditNameEditText.setText(store.name)
        }
    }

    private fun loadOpeningHoursToView(openingHours: List<OpeningHour>) {
        if (openingHours.isNotEmpty()) {
            openingHours.forEach {
                when (it.day) {
                    "monday" -> storeMondayOpenSwitch.isChecked = true
                    "tuesday" -> storeTuesdayOpenSwitch.isChecked = true
                    "wednesday" -> storeWednesdayOpenSwitch.isChecked = true
                    "thursday" -> storeThursdayOpenSwitch.isChecked = true
                    "friday" -> storeFridayOpenSwitch.isChecked = true
                    "saturday" -> storeSaturdayOpenSwitch.isChecked = true
                    "sunday" -> storeSundayOpenSwitch.isChecked = true
                }
            }

            // Aktuell unterstützen wir nur eine einzige Öffnungszeit, daher wird die Erstbeste genommen
            startDate = openingHours[0].from
            storePickOpeningStartLabel.text = startDate

            endDate = openingHours[0].to
            storePickOpeningEndLabel.text = endDate
        }
    }

    private fun getListOfStoreOpenSwitches(): List<SwitchCompat> {
        return listOf(
            storeMondayOpenSwitch,
            storeTuesdayOpenSwitch,
            storeWednesdayOpenSwitch,
            storeThursdayOpenSwitch,
            storeFridayOpenSwitch,
            storeSaturdayOpenSwitch,
            storeSundayOpenSwitch
        )
    }

    @SuppressLint("MissingPermission")
    private fun retrieveCurrentLocation() {
        fusedLocationProvider.lastLocation.addOnSuccessListener {
            // TODO: Do something with the data
            storeEditLocation.text =
                getString(R.string.locationCoordinateTemplate, it.latitude, it.longitude)
            loadedStore = loadedStore.copy(latitude = it.latitude, longitude = it.longitude, stripeAccountId = "bla")
        }.addOnFailureListener {
            showSnackbar(getString(R.string.locationRetrievalError))
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                retrieveCurrentLocation()
            } else {
                showSnackbar(getString(R.string.locationPermissionDeniedError))
            }
        }
    }

    private fun saveData() {
        if (startDate == null || endDate == null || getListOfStoreOpenSwitches().all { !it.isChecked }) {
            showSnackbar(getString(R.string.openingTimesEmptyError))
        } else {
            if (fromRegistration) {
                createStore()
            } else {
                updateStore()
            }
        }
    }

    private fun createStore() {
        context?.getPrefs()?.getVendorId()?.let { vendorId ->
            RkgClient.service.createStore(
                CreateStoreRequestModel(
                    storeEditNameEditText.text.toString(),
                    vendorId,
                    loadedStore.latitude,
                    loadedStore.longitude
                )
            ).enqueue(object : Callback<Store> {
                override fun onFailure(call: Call<Store>, t: Throwable) {
                    Snackbar.make(rootViewStoreEdit, R.string.genericError, Snackbar.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<Store>, response: Response<Store>) {
                    if (response.isSuccessful) {
                        val createdStore = response.body() ?: let {
                            Snackbar.make(
                                    rootViewStoreEdit,
                                    R.string.shopDataSaved,
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                            return
                        }
                        createdStore._id?.let {
                            context?.getPrefs()?.putStoreId(it)
                            loadedStore = loadedStore.copy(_id = it, vendor = createdStore.vendor, name = createdStore.name, stripeAccountId = "bla")
                        }
                        updateStore()
                    } else {
                        Snackbar.make(rootViewStoreEdit, R.string.genericError, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }
    }

    private fun updateStore() {
        loadedStore = loadedStore.copy(name = storeEditNameEditText.text.toString(), stripeAccountId = "bla")
        context?.getPrefs()?.getStoreId()?.let { storeId ->
            RkgClient.service.updateStore(storeId, loadedStore)
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Snackbar.make(
                                rootViewStoreEdit,
                                R.string.genericError,
                                Snackbar.LENGTH_SHORT
                            )
                            .show()
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Snackbar.make(
                                    rootViewStoreEdit,
                                    R.string.shopDataSaved,
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                            findNavController().navigate(R.id.action_storeEditFragment_to_storeFragment)
                        } else {
                            Snackbar.make(
                                    rootViewStoreEdit,
                                    R.string.genericError,
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                })
        }
    }

    private fun showSnackbar(errorText: String) {
        Snackbar.make(
            rootViewStoreEdit,
            errorText,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val KEY_FROM_REGISTRATION = "fromRegistration"
        const val PERMISSION_REQUEST_CODE = 43652
    }
}
