package com.capstoneproject.noqapp.admin.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.adapter.ListOrderAdapter
import com.capstoneproject.noqapp.admin.model.MainAdminViewModel
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityMainAdminBinding
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var rvOrders: RecyclerView
    private val list = ArrayList<Order>()
    private val timeInterval = 2000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
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

    private fun setupViewModel() {
        mainAdminViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainAdminViewModel::class.java]

        mainAdminViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || !user.isAdmin || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        rvOrders = binding.rvOrders
        rvOrders.setHasFixedSize(true)

        list.addAll(listOrders)

        rvOrders.layoutManager = LinearLayoutManager(this)

        val listOrderAdapter = ListOrderAdapter(list)
        rvOrders.adapter = listOrderAdapter
    }

    private val listOrders: ArrayList<Order>
        get() {
            val dataTime = resources.getStringArray(R.array.timeStamp)
            val dataCode = resources.getStringArray(R.array.tableCode)
            val dataName = resources.getStringArray(R.array.custName)
            val dataPrice = resources.getStringArray(R.array.totalPrice)
            val dataListOrder = resources.getStringArray(R.array.listOrder)
            val listOrder = ArrayList<Order>()
            for (i in dataName.indices) {
                val order =
                    Order(dataTime[i], dataCode[i], dataName[i], dataPrice[i], dataListOrder[i])
                listOrder.add(order)
            }
            return listOrder
        }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainAdminViewModel.logout()
            Toast.makeText(this@MainAdminActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (mBackPressed + timeInterval > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, getString(R.string.back_btn), Toast.LENGTH_SHORT).show()
        }
        mBackPressed = System.currentTimeMillis()
    }
}