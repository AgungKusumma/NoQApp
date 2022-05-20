package com.capstoneproject.noqapp.admin

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.adapter.ListOrderAdapter
import com.capstoneproject.noqapp.databinding.ActivityMainAdminBinding
import com.capstoneproject.noqapp.admin.model.Order

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
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
        rvHeroes = binding.rvOrders
        rvHeroes.setHasFixedSize(true)

        list.addAll(listOrders)

        rvHeroes.layoutManager = LinearLayoutManager(this)

        val listOrderAdapter = ListOrderAdapter(list)
        rvHeroes.adapter = listOrderAdapter
    }

    private val listOrders: ArrayList<Order>
        get() {
            val dataTime = resources.getStringArray(R.array.timeStamp)
            val dataCode = resources.getStringArray(R.array.tableCode)
            val dataName = resources.getStringArray(R.array.custName)
            val dataPrice = resources.getStringArray(R.array.totalPrice)
            val dataListOrder = resources.getStringArray(R.array.listOrder)
            val listHero = ArrayList<Order>()
            for (i in dataName.indices) {
                val hero =
                    Order(dataTime[i], dataCode[i], dataName[i], dataPrice[i], dataListOrder[i])
                listHero.add(hero)
            }
            return listHero
        }
}