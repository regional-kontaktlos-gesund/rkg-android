package org.wirvsvirus.rkg.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.main_fragment.*
import org.wirvsvirus.rkg.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        mainMap.onCreate(savedInstanceState)
        mainMap.getMapAsync { map ->
            Log.d("main", "map is ready!")
        }
    }

    override fun onResume() {
        super.onResume()
        mainMap.onResume()
    }

    override fun onPause() {
        mainMap.onPause()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        mainMap.onStart()
    }

    override fun onStop() {
        mainMap.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mainMap.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        mainMap?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mainMap.onLowMemory()
        super.onLowMemory()
    }

}
