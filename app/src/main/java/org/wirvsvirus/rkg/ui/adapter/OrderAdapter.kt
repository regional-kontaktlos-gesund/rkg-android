package org.wirvsvirus.rkg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.Order

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    var items: List<Order> = emptyList()
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.codeWord.text = item.code

        item.items.forEach {
            val orderProduct = View.inflate(holder.orderItemContainer.context, R.layout.list_item_order_product, null)
            val orderProductIcon: ImageView = orderProduct.findViewById(R.id.orderItemProductIcon)
            val orderProductText: TextView = orderProduct.findViewById(R.id.orderItemProductText)

            orderProductText.text = "${it.amount}x ${it.name}"

            holder.orderItemContainer.addView(orderProduct)
        }
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val codeWord: TextView = rootView.findViewById(R.id.orderItemCodeWord)
        val orderItemContainer: LinearLayout = rootView.findViewById(R.id.orderItemContainer)
    }
}