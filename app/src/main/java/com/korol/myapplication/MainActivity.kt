package com.korol.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korol.myapplication.databinding.ActivityMainBinding
import com.korol.myapplication.extentions.addInsets
import com.korol.myapplication.extentions.setColorStatusBar

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main_activity) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        initInsets()
    }

    private fun initInsets() {
        viewBinding.activityContainer.addInsets()
        this.setColorStatusBar(viewBinding.activityContainer, null, true)
    }
}