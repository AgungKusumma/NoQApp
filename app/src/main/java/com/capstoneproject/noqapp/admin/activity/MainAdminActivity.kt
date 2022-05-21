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
import com.capstoneproject.noqapp.ViewModelFactory
import com.capstoneproject.noqapp.admin.adapter.ListOrderAdapter
import com.capstoneproject.noqapp.admin.model.MainAdminViewModel
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityMainAdminBinding
import com.capstoneproject.noqapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<Order>()

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
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
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

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainAdminViewModel.logout()
            Toast.makeText(this@MainAdminActivity,
                getString(R.string.logout_success), Toast.LENGTH_LONG).show()
        }
    }
}