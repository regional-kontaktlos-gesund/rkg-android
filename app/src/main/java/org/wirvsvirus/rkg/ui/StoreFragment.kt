package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_store.*
import org.wirvsvirus.rkg.*
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoreFragment : Fragment() {

    private var loadedStore: Store? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if ((activity as? MainActivity)?.isBottomNavShown() == false) {
            (activity as? MainActivity)?.showBottomNav()
        }

        (activity as? MainActivity)?.setSelectedBottomNavItem(R.id.navStore)

        storeMap.onCreate(savedInstanceState)

        context?.getPrefs()?.getStoreId()?.let {
            RkgClient.service.getStore(it).enqueue(object : Callback<Store> {
                override fun onFailure(call: Call<Store>, t: Throwable) {
                    Snackbar.make(storeRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Store>, response: Response<Store>) {
                    loadedStore = response.body() ?: let {
                        Snackbar.make(storeRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                            .show()
                        return
                    }
                    loadStoreDataToView()
                }
            })
        }
    }

    private fun loadStoreDataToView() {
        loadedStore?.let { store ->
            val storePosition = LatLng(store.latitude, store.longitude)
            storeMap.getMapAsync { map ->
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(storePosition, 16.0f)
                )
                map.addMarker(MarkerOptions().position(storePosition).title(store.name))
                    .showInfoWindow()
            }

            val monday = store.openingHours.firstOrNull { it.day == "monday" }
            val tuesday = store.openingHours.firstOrNull { it.day == "tuesday" }
            val wednesday = store.openingHours.firstOrNull { it.day == "wednesday" }
            val thursday = store.openingHours.firstOrNull { it.day == "thursday" }
            val friday = store.openingHours.firstOrNull { it.day == "friday" }
            val saturday = store.openingHours.firstOrNull { it.day == "saturday" }
            val sunday = store.openingHours.firstOrNull { it.day == "sunday" }

            monday?.let {
                storeOpeningTimesMonday.text =
                    getString(R.string.opening_hours_template, monday.from, monday.to)
            } ?: run { storeOpeningTimesMonday.text = getString(R.string.closed) }
            tuesday?.let {
                storeOpeningTimesTuesday.text =
                    getString(R.string.opening_hours_template, tuesday.from, tuesday.to)
            } ?: run { storeOpeningTimesTuesday.text = getString(R.string.closed) }
            wednesday?.let {
                storeOpeningTimesWednesday.text =
                    getString(R.string.opening_hours_template, wednesday.from, wednesday.to)
            } ?: run { storeOpeningTimesWednesday.text = getString(R.string.closed) }
            thursday?.let {
                storeOpeningTimesThursday.text =
                    getString(R.string.opening_hours_template, thursday.from, thursday.to)
            } ?: run { storeOpeningTimesThursday.text = getString(R.string.closed) }
            friday?.let {
                storeOpeningTimesFriday.text =
                    getString(R.string.opening_hours_template, friday.from, friday.to)
            } ?: run { storeOpeningTimesFriday.text = getString(R.string.closed) }
            saturday?.let {
                storeOpeningTimesSaturday.text =
                    getString(R.string.opening_hours_template, saturday.from, saturday.to)
            } ?: run { storeOpeningTimesSaturday.text = getString(R.string.closed) }
            sunday?.let {
                storeOpeningTimesSunday.text =
                    getString(R.string.opening_hours_template, sunday.from, sunday.to)
            } ?: run { storeOpeningTimesSunday.text = getString(R.string.closed) }

            storeOwnerEmail.text = context?.getPrefs()?.getVendorEmail() ?: ""
            storeOwnerName.text = store.name

            storeEdit.setOnClickListener {
                val bundle = Bundle().apply { putSerializable(KEY_STORE_OBJECT, store) }
                findNavController().navigate(R.id.action_storeFragment_to_storeEditFragment, bundle)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        storeMap.onResume()
    }

    override fun onPause() {
        storeMap.onPause()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        storeMap.onStart()
    }

    override fun onStop() {
        storeMap.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        storeMap.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        storeMap?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        storeMap.onLowMemory()
        super.onLowMemory()
    }

    companion object {
        const val KEY_STORE_OBJECT = "storeObject"
    }
}
