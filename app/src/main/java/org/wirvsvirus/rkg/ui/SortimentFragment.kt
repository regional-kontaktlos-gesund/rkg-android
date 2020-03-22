package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.util.Log
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
import org.wirvsvirus.rkg.model.Product
import org.wirvsvirus.rkg.ui.adapter.SortimentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToLong

class SortimentFragment : Fragment() {

    private val adapter = SortimentAdapter()
    private var addEditDialog: AlertDialog? = null
    private var isAdding: Boolean = true

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
            isAdding = true
            addEditDialog = AlertDialog.Builder(requireContext())
                .setView(R.layout.dialog_add_edit_product)
                .setPositiveButton(R.string.save) { _, _ ->
                    saveData()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
            addEditDialog?.show()
        }
    }

    private fun loadProducts(scrollToBottom: Boolean) {
        // TODO use our storeId
        RkgClient.service.getProducts("5e7637033530e88ed953fd1c").enqueue(object : Callback<List<Product>> {
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
        val amount = dialog.findViewById<TextInputEditText>(R.id.productAmount)?.text!!.toString()
        val name = dialog.findViewById<TextInputEditText>(R.id.productName)?.text!!.toString()
        val priceString = dialog.findViewById<TextInputEditText>(R.id.productPrice)?.text!!.toString()
        val price = (priceString.replace(',', '.').toDouble() * 100).roundToLong()

        val newProduct = Product(
            name, type, amount, price, "none"
        )

        // TODO use our storeId
        RkgClient.service.addProduct("5e7637033530e88ed953fd1c", newProduct).enqueue(object : Callback<Void>{
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
    }
}