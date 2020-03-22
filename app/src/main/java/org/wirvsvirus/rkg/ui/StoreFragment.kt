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
import org.wirvsvirus.rkg.MainActivity
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.getPrefs
import org.wirvsvirus.rkg.getVendorId
import org.wirvsvirus.rkg.model.OpeningHour
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

        context?.getPrefs()?.getVendorId()?.let {
            RkgClient.service.getStore(it).enqueue(object : Callback<Store> {
                override fun onFailure(call: Call<Store>, t: Throwable) {
                    Snackbar.make(storeRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Store>, response: Response<Store>) {
                    loadedStore = response.body() ?: let {
                        // TODO: Mock entfernen, Snackbar und return wieder einkommentieren
                        getMockedStore()
                        // Snackbar.make(storeRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                        // return
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
            }
            tuesday?.let {
                storeOpeningTimesTuesday.text =
                    getString(R.string.opening_hours_template, tuesday.from, tuesday.to)
            }
            wednesday?.let {
                storeOpeningTimesWednesday.text =
                    getString(R.string.opening_hours_template, wednesday.from, wednesday.to)
            }
            thursday?.let {
                storeOpeningTimesThursday.text =
                    getString(R.string.opening_hours_template, thursday.from, thursday.to)
            }
            friday?.let {
                storeOpeningTimesFriday.text =
                    getString(R.string.opening_hours_template, friday.from, friday.to)
            }
            saturday?.let {
                storeOpeningTimesSaturday.text =
                    getString(R.string.opening_hours_template, saturday.from, saturday.to)
            }
            sunday?.let {
                storeOpeningTimesSunday.text =
                    getString(R.string.opening_hours_template, sunday.from, sunday.to)
            }

            storeOwnerEmail.text = store.vendor
            storeOwnerName.text = store.name

            storeEdit.setOnClickListener {
                findNavController().navigate(R.id.action_storeFragment_to_storeEditFragment)
            }
        }
    }

    private fun getMockedStore(): Store {
        val openingHours = listOf(
            OpeningHour("monday", "07:00", "14:00"),
            OpeningHour("tuesday", "10:00", "18:00"),
            OpeningHour("wednesday", "13:30", "19:30"),
            OpeningHour("thursday", "15:00", "20:00"),
            OpeningHour("friday", "09:00", "13:00"),
            OpeningHour("saturday", "06:30", "12:00"),
            OpeningHour("sunday", "06:00", "11:30")
        )

        return Store(
            "1",
            "Hans Luft - Erdbeeren",
            "hans.luft@erdbeerbauer.de",
            50.107795,
            8.651872,
            "stripe",
            emptyList(),
            openingHours,
            true
        )
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

}
