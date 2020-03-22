package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_store.*
import org.wirvsvirus.rkg.MainActivity
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.OpeningHour

class StoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if ((activity as? MainActivity)?.isBottomNavShown() == false) {
            (activity as? MainActivity)?.showBottomNav()
        }

        (activity as? MainActivity)?.setSelectedBottomNavItem(R.id.navStore)

        storeMap.onCreate(savedInstanceState)
        storeMap.getMapAsync { map ->
            Log.d("main", "map is ready!")
        }

        val openingHours = listOf(
            OpeningHour("monday", "07:00", "14:00"),
            OpeningHour("tuesday", "10:00", "18:00"),
            OpeningHour("wednesday", "13:30", "19:30"),
            OpeningHour("thursday", "15:00", "20:00"),
            OpeningHour("friday", "09:00", "13:00"),
            OpeningHour("saturday", "06:30", "12:00"),
            OpeningHour("sunday", "06:00", "11:30")
        )

        val monday = openingHours.firstOrNull { it.day == "monday" }
        val tuesday = openingHours.firstOrNull { it.day == "tuesday" }
        val wednesday = openingHours.firstOrNull { it.day == "wednesday" }
        val thursday = openingHours.firstOrNull { it.day == "thursday" }
        val friday = openingHours.firstOrNull { it.day == "friday" }
        val saturday = openingHours.firstOrNull { it.day == "saturday" }
        val sunday = openingHours.firstOrNull { it.day == "sunday" }

        monday?.let {
            storeOpeningTimesMonday.text = getString(R.string.opening_hours_template, monday.from, monday.to)
        }
        tuesday?.let {
            storeOpeningTimesTuesday.text = getString(R.string.opening_hours_template, tuesday.from, tuesday.to)
        }
        wednesday?.let {
            storeOpeningTimesWednesday.text = getString(R.string.opening_hours_template, wednesday.from, wednesday.to)
        }
        thursday?.let {
            storeOpeningTimesThursday.text = getString(R.string.opening_hours_template, thursday.from, thursday.to)
        }
        friday?.let {
            storeOpeningTimesFriday.text = getString(R.string.opening_hours_template, friday.from, friday.to)
        }
        saturday?.let {
            storeOpeningTimesSaturday.text = getString(R.string.opening_hours_template, saturday.from, saturday.to)
        }
        sunday?.let {
            storeOpeningTimesSunday.text = getString(R.string.opening_hours_template, sunday.from, sunday.to)
        }

        storeOwnerEmail.text = "hans@luft.de"
        storeOwnerName.text = "Hans Luft - Erdbeeren"

        storeEdit.setOnClickListener {
            findNavController().navigate(R.id.action_storeFragment_to_storeEditFragment)
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

}
