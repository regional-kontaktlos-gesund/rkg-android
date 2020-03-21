package org.wirvsvirus.rkg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.main_activity.*
import org.wirvsvirus.rkg.ui.SortimentFragment
import org.wirvsvirus.rkg.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        bottomNav.setOnNavigationItemSelectedListener {
            val dest = when (it.itemId) {
                R.id.navSortiment -> R.id.sortimentFragment
                // ...
                else -> -1
            }

            if (dest > -1) {
                findNavController(R.id.navHost).navigate(dest)
            }

            return@setOnNavigationItemSelectedListener true
        }
    }
}
