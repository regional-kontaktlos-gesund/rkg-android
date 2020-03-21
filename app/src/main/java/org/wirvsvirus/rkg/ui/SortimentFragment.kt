package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_sortiment.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.SortimentItem
import org.wirvsvirus.rkg.ui.adapter.SortimentAdapter

class SortimentFragment : Fragment() {

    private val adapter = SortimentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sortiment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortimentList.layoutManager = LinearLayoutManager(context)
        sortimentList.adapter = adapter

        // TODO remove mock
        adapter.items = listOf(
            SortimentItem("Erdbeeren", "errdbeeren", 300, 250, "500g"),
            SortimentItem("Spargel", "spargel", 500, 100, "500g")
        )
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = SortimentFragment()
    }
}