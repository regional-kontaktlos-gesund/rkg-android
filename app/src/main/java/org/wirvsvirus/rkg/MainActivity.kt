package org.wirvsvirus.rkg

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
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

        bottomNav.setOnNavigationItemReselectedListener {
            // Do nothing here to avoid recreation of fragments
        }
    }

    fun hideBottomNav() {
        bottomNav.visibility = View.GONE
    }

    fun showBottomNav() {
        bottomNav.visibility = View.VISIBLE
    }

    fun isBottomNavShown(): Boolean = bottomNav.visibility == View.VISIBLE

    fun setSelectedBottomNavItem(itemId: Int) {
        bottomNav.menu.iterator().forEach {
            it.isChecked = it.itemId == itemId
        }
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHost).navigateUp()
}
