package com.capstoneproject.noqapp.main.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.adapter.ListDetailOrderAdapter
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityDetailHistoryBinding
import com.capstoneproject.noqapp.main.viewmodel.DetailHistoryViewModel
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory
import java.text.NumberFormat
import java.util.*

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailHistoryViewModel: DetailHistoryViewModel
    private lateinit var adapter: ListDetailOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupData()
        showRecyclerList()
        setupAction()
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
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        detailHistoryViewModel = DetailHistoryViewModel.getInstance(this)

        val customer = getString(R.string.role_customer)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != customer || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val order = intent.getStringExtra("Order")

        mainViewModel.getUser().observe(this) { user ->
            if (order != null) {
                detailHistoryViewModel.getDetailOrderHistory(order, user.token)
            }
        }
    }

    private fun showRecyclerList() {
        val localeID = Locale("in", "ID")
        val nf: NumberFormat = NumberFormat.getInstance(localeID)

        adapter = ListDetailOrderAdapter()

        binding.apply {
            rvOrders.layoutManager = LinearLayoutManager(this@DetailHistoryActivity)
            rvOrders.setHasFixedSize(true)
            rvOrders.adapter = adapter
        }

        detailHistoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailHistoryViewModel.order.observe(this) {
            val totalPrice = nf.format(it.totalPrice)

            binding.apply {
                tvItemCode.text = getString(R.string.table_code, it.tableId)
                tvTotalPrice.text = getString(R.string.total_price, totalPrice)
                tvItemStatus.text = getString(R.string.status, it.status)
            }
        }

        detailHistoryViewModel.menu.observe(this) {
            adapter.setListOrder(it)
        }

        detailHistoryViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (error) {
                    binding.apply {
                        ivEmpty.isVisible = true
                        progressBar.isVisible = false
                        rvOrders.adapter = null
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.fabHome.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}