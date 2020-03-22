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
import org.wirvsvirus.rkg.model.OrderWithProducts
import java.text.NumberFormat
import java.util.*

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

    var items: MutableList<OrderWithProducts> = mutableListOf()
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.codeWord.text = item.code
        holder.orderTotalPrice.text = holder.orderTotalPrice.resources.getString(R.string.priceTemplate, formatter.format(item.sumTotal/100))

        holder.orderItemContainer.removeAllViews()
        item.items.forEach {
            val orderProduct = View.inflate(holder.orderItemContainer.context, R.layout.list_item_order_product, null)
            val orderProductIcon: ImageView = orderProduct.findViewById(R.id.orderItemProductIcon)
            val orderProductText: TextView = orderProduct.findViewById(R.id.orderItemProductText)

            orderProductText.text = "${it.amount}x ${it.product.name}"
            orderProductIcon.setImageResource(mapTypeToImage(it.product.type))

            holder.orderItemContainer.addView(orderProduct)
        }
    }

    private fun mapTypeToImage(type: String?): Int {
        return when (type) {
            "Spargel" -> R.drawable.illu_asparagus
            "Erdbeere" -> R.drawable.illu_strawberry
            else -> R.drawable.ic_launcher
        }
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val codeWord: TextView = rootView.findViewById(R.id.orderItemCodeWord)
        val orderItemContainer: LinearLayout = rootView.findViewById(R.id.orderItemContainer)
        val orderTotalPrice: TextView = rootView.findViewById(R.id.orderTotalPrice)
    }
}