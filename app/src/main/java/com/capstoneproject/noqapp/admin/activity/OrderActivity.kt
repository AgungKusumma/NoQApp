package com.capstoneproject.noqapp.admin.activity

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.fabHome.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupData() {
        val order = intent.getParcelableExtra<Order>("Order") as Order
        binding.apply {
            tvItemTimeStamp.text = order.timeStamp
            tvItemCode.text = getString(R.string.table_code, order.tableCode)
            tvItemName.text = order.username
            tvItemOrder.text = order.orderMenu
            tvItemPrice.text = getString(R.string.total_price, order.price)
        }
    }
}