package org.wirvsvirus.rkg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.SortimentItem
import java.text.NumberFormat
import java.util.*

class SortimentAdapter : RecyclerView.Adapter<SortimentAdapter.ViewHolder>() {

    private val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

    var items: List<SortimentItem> = emptyList()
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_sortiment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.product.text = item.name
        holder.price.text = formatter.format(item.price/100)
        holder.amount.text = item.amount
        holder.availability.text = item.available.toString()
        holder.icon.setImageResource(R.drawable.ic_launcher) // TODO map to real icons
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val icon: ImageView = rootView.findViewById(R.id.sortimentIcon)
        val price: TextView = rootView.findViewById(R.id.sortimentPriceValue)
        val availability: TextView = rootView.findViewById(R.id.sortimentAvailiabilityValue)
        val amount: TextView = rootView.findViewById(R.id.sortimentAmountValue)
        val product: TextView = rootView.findViewById(R.id.sortimentProduct)
    }
}