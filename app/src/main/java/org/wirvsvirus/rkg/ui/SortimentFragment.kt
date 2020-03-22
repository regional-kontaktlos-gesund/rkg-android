package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_sortiment.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.getPrefs
import org.wirvsvirus.rkg.getStoreId
import org.wirvsvirus.rkg.model.Product
import org.wirvsvirus.rkg.ui.adapter.SortimentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import kotlin.math.roundToLong

class SortimentFragment : Fragment() {

    private val adapter = SortimentAdapter()
    private var addEditDialog: AlertDialog? = null
    private var editingProduct: Product? = null

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

        loadProducts(false)

        fabAddItem.setOnClickListener {
            showAddEditDialog()
        }

        adapter.editListener = { product ->
            editingProduct = product
            showAddEditDialog()
        }

        adapter.deleteListener = { product ->
            showDeleteDialog(product)
        }

        adapter.availabilityListener = { product, chipId ->
            updateAvailability(product, chipId)
        }
    }

    private fun updateAvailability(product: Product, chipId: Int) {
        val availability = when(chipId) {
            R.id.sortimentAvailabilityNone -> "none"
            R.id.sortimentAvailabilityLow -> "low"
            R.id.sortimentAvailabilityMedium -> "medium"
            R.id.sortimentAvailabilityFull -> "full"
            else -> return
        }

        val newProduct = product.copy(stock = availability)
        RkgClient.service.updateProduct(requireContext().getPrefs().getStoreId()!!, newProduct._id!!, newProduct).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    onFailure(call, Throwable())
                    return
                }
                Snackbar.make(sortimentRoot, R.string.changedSuccessfully, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDeleteDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.deleteConfirm)
            .setPositiveButton(R.string.delete) { _, _  -> deleteProduct(product) }
            .setNegativeButton(R.string.cancel) { _, _  -> }
            .show()
    }

    private fun deleteProduct(product: Product) {
        RkgClient.service.deleteProduct(requireContext().getPrefs().getStoreId()!!, product._id!!).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    onFailure(call, Throwable())
                    return
                }
                loadProducts(false)
            }
        })
    }

    private fun showAddEditDialog() {
        addEditDialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_add_edit_product)
            .setPositiveButton(R.string.save) { _, _ ->
                saveData()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .create()
        addEditDialog?.show()

        if (editingProduct != null) {
            val dialog = addEditDialog!!
            val product = editingProduct!!
            val items = requireContext().resources.getStringArray(R.array.productTypes)
            dialog.findViewById<AppCompatSpinner>(R.id.productTypeSpinner)!!.setSelection(items.indexOfFirst { it == product.type })
            dialog.findViewById<TextInputEditText>(R.id.storeEditNameEditText)!!.setText(product.unit)
            dialog.findViewById<TextInputEditText>(R.id.productName)!!.setText(product.name)
            dialog.findViewById<TextInputEditText>(R.id.productPrice)!!.setText(NumberFormat.getNumberInstance().format(product.price/100f).replace('.', ','))
        }

    }

    private fun loadProducts(scrollToBottom: Boolean) {
        RkgClient.service.getProducts(requireContext().getPrefs().getStoreId()!!).enqueue(object : Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val products = response.body() ?: run {
                    Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                    return
                }

                adapter.items = products
                adapter.notifyDataSetChanged()
                if (scrollToBottom) {
                    sortimentList.scrollToPosition(adapter.itemCount-1)
                }
            }
        })
    }

    private fun saveData() {
        if (addEditDialog == null) {
            return
        }
        val dialog = addEditDialog!!

        val type = dialog.findViewById<AppCompatSpinner>(R.id.productTypeSpinner)?.selectedItem as String
        val amount = dialog.findViewById<TextInputEditText>(R.id.storeEditNameEditText)?.text!!.toString()
        val name = dialog.findViewById<TextInputEditText>(R.id.productName)?.text!!.toString()
        val priceString = dialog.findViewById<TextInputEditText>(R.id.productPrice)?.text!!.toString()
        val price = (priceString.replace(',', '.').toDouble() * 100).roundToLong()

        if (editingProduct == null) {
            val newProduct = Product(
                null, name, type, amount, price, "none"
            )
            RkgClient.service.addProduct(requireContext().getPrefs().getStoreId()!!, newProduct).enqueue(object : Callback<Void>{
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        onFailure(call, Throwable())
                        return
                    }

                    loadProducts(true)
                }
            })
        } else {
            val newProduct = Product(
                editingProduct!!._id, name, type, amount, price, editingProduct!!.stock
            )
            RkgClient.service.updateProduct(requireContext().getPrefs().getStoreId()!!, editingProduct!!._id!!, newProduct).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        onFailure(call, Throwable())
                        return
                    }

                    loadProducts(false)
                }
            })
        }
    }
}