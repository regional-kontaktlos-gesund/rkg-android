package org.wirvsvirus.rkg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        bottomNav.setOnNavigationItemSelectedListener {
            val dest = when (it.itemId) {
                R.id.navSortiment -> R.id.sortimentFragment
                R.id.navStore -> R.id.storeFragment
                R.id.navOrders -> R.id.orderFragment
                // ...
                else -> -1
            }

            if (dest > -1) {
                findNavController(R.id.navHost).navigate(dest)
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }

    fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHost).navigateUp()
}
