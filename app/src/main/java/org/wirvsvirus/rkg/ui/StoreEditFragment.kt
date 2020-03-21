package org.wirvsvirus.rkg.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_store_edit.*
import org.wirvsvirus.rkg.R

class StoreEditFragment : Fragment() {

    private var startDate: String? = null
    private var endDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_store_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storePickOpeningStartButton.setOnClickListener {
            val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startDate = String.format("%02d:%02d Uhr", hourOfDay, minute)
                storePickOpeningStartLabel.text = startDate
            }
            TimePickerDialog(activity, listener, 6, 0, DateFormat.is24HourFormat(activity)).show()
        }

        storePickOpeningEndButton.setOnClickListener {
            val listener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                endDate = String.format("%02d:%02d Uhr", hourOfDay, minute)
                storePickOpeningEndLabel.text = endDate
            }
            TimePickerDialog(activity, listener, 18, 0, DateFormat.is24HourFormat(activity)).show()
        }

        storeEditSave.setOnClickListener {
            if (startDate == null || endDate == null || getListOfStoreOpenSwitches().all { !it.isChecked }) {
                Snackbar.make(
                    rootViewStoreEdit,
                    "Bitte wählen Sie Ihre Öffnungszeiten aus",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    rootViewStoreEdit,
                    "Öffnungszeiten gespeichert",
                    Snackbar.LENGTH_SHORT
                ).show()

                // TODO API-Call einbauen
            }
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
}
