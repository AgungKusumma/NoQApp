package com.capstoneproject.noqapp.main.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.databinding.ActivityDetailOrderBinding
import com.capstoneproject.noqapp.main.adapter.ListUserOrderAdapter
import com.capstoneproject.noqapp.model.ItemMenu
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var rvItemOrder: RecyclerView
    private val list = ArrayList<ItemMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        showRecyclerList()
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

    private fun showRecyclerList() {
        val itemMenu = intent.getParcelableArrayListExtra<ItemMenu>("ItemOrder")

        Log.e("Detail ", "$itemMenu")
        rvItemOrder = binding.rvItemOrder
        rvItemOrder.setHasFixedSize(true)

        itemMenu?.let { list.addAll(it) }
        rvItemOrder.layoutManager = LinearLayoutManager(this)

        val listOrderAdapter = ListUserOrderAdapter(list)
        rvItemOrder.adapter = listOrderAdapter

        var subTotalAmount = 0
        val localeID = Locale("in", "ID")
        val nf: NumberFormat = NumberFormat.getInstance(localeID)

        if (itemMenu != null) {
            for (i in 0..itemMenu.size.minus(1)) {

                val price = itemMenu[i]

                subTotalAmount += price.price * price.totalInCart

                Log.e("Detail Price ", "$subTotalAmount")
                Log.e("PriceMenu ", "${price?.price}")

            }
            ("Rp. " + nf.format(subTotalAmount)).also { binding.tvTotalPrice.text = it }
        }
    }

}