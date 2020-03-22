package org.wirvsvirus.rkg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.model.Product
import java.text.NumberFormat
import java.util.*

class SortimentAdapter : RecyclerView.Adapter<SortimentAdapter.ViewHolder>() {

    private val formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY)

    var items: List<Product> = emptyList()
    var editListener: ((Product) -> Unit)? = null
    var deleteListener: ((Product) -> Unit)? = null
    var availabilityListener: ((Product, Int) -> Unit)? = null
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_sortiment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.product.text = item.name
        holder.price.text = formatter.format(item.price/100f)
        holder.amount.text = item.unit

        when(item.stock) {
            "full" -> holder.availabilityFull.isChecked = true
            "medium" -> holder.availabilityMedium.isChecked = true
            "low" -> holder.availabilityLow.isChecked = true
            "none" -> holder.availabilityNone.isChecked = true
        }

        val illusRes = when(item.type) {
            "Erdbeere" -> R.drawable.illu_strawberry
            "Spargel" -> R.drawable.illu_asparagus
            else -> R.drawable.ic_launcher
        }
        holder.icon.setImageResource(illusRes)
        holder.editButton.setOnClickListener {
            editListener?.invoke(item)
        }
        holder.deleteButton.setOnClickListener {
            deleteListener?.invoke(item)
        }
        holder.availabilityGroup.setOnCheckedChangeListener { _, checkedId ->
            availabilityListener?.invoke(item, checkedId)
        }
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val icon: ImageView = rootView.findViewById(R.id.sortimentIcon)
        val price: TextView = rootView.findViewById(R.id.sortimentPriceValue)
        val availabilityGroup: ChipGroup = rootView.findViewById(R.id.sortimentAvailabilityGroup)
        val availabilityFull: Chip = rootView.findViewById(R.id.sortimentAvailabilityFull)
        val availabilityMedium: Chip = rootView.findViewById(R.id.sortimentAvailabilityMedium)
        val availabilityLow: Chip = rootView.findViewById(R.id.sortimentAvailabilityLow)
        val availabilityNone: Chip = rootView.findViewById(R.id.sortimentAvailabilityNone)
        val amount: TextView = rootView.findViewById(R.id.sortimentAmountValue)
        val product: TextView = rootView.findViewById(R.id.sortimentProduct)
        val editButton: Button = rootView.findViewById(R.id.sortimentEditButton)
        val deleteButton: Button = rootView.findViewById(R.id.sortimentDeleteButton)
    }
}