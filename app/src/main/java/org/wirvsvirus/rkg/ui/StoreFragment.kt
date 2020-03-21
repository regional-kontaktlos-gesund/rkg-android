package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_store.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.OpeningHour
import org.wirvsvirus.rkg.model.OpeningHours

class StoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        storeMap.onCreate(savedInstanceState)
        storeMap.getMapAsync { map ->
            Log.d("main", "map is ready!")
        }

        val openingHours = OpeningHours(
            OpeningHour("07:00", "14:00"),
            OpeningHour("10:00", "18:00"),
            OpeningHour("13:30", "19:30"),
            OpeningHour("15:00", "20:00"),
            OpeningHour("09:00", "13:00"),
            OpeningHour("06:30", "12:00"),
            OpeningHour("06:00", "11:30")
        )

        storeOpeningTimesMonday.text = getString(R.string.opening_hours_template, openingHours.monday.from, openingHours.monday.to)
        storeOpeningTimesTuesday.text = getString(R.string.opening_hours_template, openingHours.tuesday.from, openingHours.tuesday.to)
        storeOpeningTimesWednesday.text = getString(R.string.opening_hours_template, openingHours.wednesday.from, openingHours.wednesday.to)
        storeOpeningTimesThursday.text = getString(R.string.opening_hours_template, openingHours.thursday.from, openingHours.thursday.to)
        storeOpeningTimesFriday.text = getString(R.string.opening_hours_template, openingHours.friday.from, openingHours.friday.to)
        storeOpeningTimesSaturday.text = getString(R.string.opening_hours_template, openingHours.saturday.from, openingHours.saturday.to)
        storeOpeningTimesSunday.text = getString(R.string.opening_hours_template, openingHours.sunday.from, openingHours.sunday.to)

        storeOwnerEmail.text = "hans@luft.de"
        storeOwnerName.text = "Hans Luft - Erdbeeren"
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
