package org.wirvsvirus.rkg.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sortiment.*
import org.wirvsvirus.rkg.R
import org.wirvsvirus.rkg.api.RkgClient
import org.wirvsvirus.rkg.model.Product
import org.wirvsvirus.rkg.ui.adapter.SortimentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // TODO use our storeId
        RkgClient.service.getProducts("5e7637033530e88ed953fd1c").enqueue(object : Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val products = response.body() ?: run {
                    Snackbar.make(sortimentRoot, R.string.genericError, Snackbar.LENGTH_SHORT)
                    return
                }

                adapter.items = products
                adapter.notifyDataSetChanged()
            }
        })
    }
}