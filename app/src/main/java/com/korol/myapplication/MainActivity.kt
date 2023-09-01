package com.korol.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.databinding.ActivityMainBinding
import com.korol.myapplication.databinding.FragmentHomeBinding
import com.korol.myapplication.extentions.addInsets
import com.korol.myapplication.extentions.setColorStatusBar

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = viewBinding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main_activity) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navView.setupWithNavController(navController)

        fun showBottomNav() {
            navView.visibility = View.VISIBLE
        }

        fun hideBottomNav() {
            navView.visibility = View.GONE
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home -> showBottomNav()
                R.id.fragment_shop -> showBottomNav()
                R.id.fragment_favorites -> showBottomNav()
                R.id.fragment_profile -> showBottomNav()
                else -> hideBottomNav()
            }
        }
        initInsets()

    }

    private fun initInsets() {
        viewBinding.activityContainer.addInsets()
        this.setColorStatusBar(viewBinding.activityContainer, null, true)
    }
}