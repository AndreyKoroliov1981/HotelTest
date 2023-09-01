package com.korol.myapplication.ui.shop

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.korol.myapplication.R
import com.korol.myapplication.app.App
import com.korol.myapplication.common.IsNotHomeData
import com.korol.myapplication.databinding.FragmentCartBinding
import com.korol.myapplication.ui.cart.CartViewModel
import com.korol.myapplication.ui.cart.CartViewModelFactory
import com.korol.myapplication.ui.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShopFragment : Fragment(R.layout.fragment_shop) {
}