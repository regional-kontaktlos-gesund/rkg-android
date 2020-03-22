package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_orders.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.api.RkgService
import org.wirvsvirus.rkg.model.Order
import org.wirvsvirus.rkg.model.OrderItem
import org.wirvsvirus.rkg.model.OrderWithProducts
import org.wirvsvirus.rkg.ui.adapter.OrderAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        adapter.items.clear()
        getOrders()
    }

    private fun getOrders() {
        RkgClient.service.getAllOrders().enqueue(object : Callback<List<Order>> {
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Snackbar.make(ordersRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
            }

            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                val allOrders = response.body() ?: run {
                    Snackbar.make(ordersRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                    return
                }

                loadOrderDetails(allOrders.toMutableList())
            }
        })
    }

    private fun loadOrderDetails(allOrders: MutableList<Order>) {
        if (allOrders.size == 0) {
            return
        }

        allOrders.first().let {
            RkgClient.service.getOrder(it._id).enqueue(object : Callback<OrderWithProducts> {
                override fun onFailure(call: Call<OrderWithProducts>, t: Throwable) {
                    Snackbar.make(ordersRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                    allOrders.removeAt(0)
                    loadOrderDetails(allOrders)
                }

                override fun onResponse(
                    call: Call<OrderWithProducts>,
                    response: Response<OrderWithProducts>
                ) {
                    val order = response.body() ?: run {
                        Snackbar.make(ordersRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                        return
                    }
                    adapter.items.add(order)
                    adapter.notifyDataSetChanged()
                    allOrders.removeAt(0)
                    loadOrderDetails(allOrders)
                }
            })
        }
    }
}