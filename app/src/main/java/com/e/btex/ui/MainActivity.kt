package com.e.btex.ui

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.e.btex.R
import com.e.btex.databinding.ActivityMainBinding
import com.e.btex.databinding.NavHeaderMainBinding
import com.e.btex.util.extensions.header
import com.e.btex.util.extensions.longToast
import com.e.btex.util.extensions.setAvailability
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    val drawerLayout: DrawerLayout
        get() = binding.drawerLayout


    val header: NavHeaderMainBinding by lazy {
        NavHeaderMainBinding.bind(binding.navView.header)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Close Application if Bluetooth module unavailable
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            longToast(R.string.error_bluetooth_unavailable)
            finish()

        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

            val host = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
            navController = host.navController

            binding.navView.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val isAvailable = when (destination.id) {
                    R.id.plotFragment -> true
                    else -> false
                }
                binding.drawerLayout.setAvailability(isAvailable)
            }

        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
