package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_orders.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.Order
import org.wirvsvirus.rkg.model.Product
import org.wirvsvirus.rkg.ui.adapter.OrderAdapter

class OrderFragment : Fragment() {

    private val adapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderList.layoutManager = LinearLayoutManager(context)
        orderList.adapter = adapter

        // TODO remove mock
        adapter.items = listOf(
            Order(0, "1", "Geheimnis1", listOf(Product("Erdbeere", "Frucht", "300", 1000, 3)), "My Store", 1000),
            Order(0, "2", "Geheimnis2", listOf(Product("Spargel", "Gemüse", "10", 5000, 2), Product("Erdbeere", "Frucht", "300", 1000, 3)), "My Store", 500),
            Order(0, "3", "Geheimnis3", listOf(Product("Erdbeere", "Frucht", "300", 1000, 3)), "My Store", 700)
        )
        adapter.notifyDataSetChanged()
    }
}