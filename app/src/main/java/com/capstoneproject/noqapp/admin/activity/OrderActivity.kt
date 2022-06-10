package com.capstoneproject.noqapp.admin.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.adapter.ListDetailOrderAdapter
import com.capstoneproject.noqapp.admin.viewmodel.DetailOrderViewModel
import com.capstoneproject.noqapp.admin.viewmodel.MainAdminViewModel
import com.capstoneproject.noqapp.admin.viewmodel.UpdateOrderViewModel
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityOrderBinding
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory
import java.text.NumberFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var detailOrderViewModel: DetailOrderViewModel
    private lateinit var updateOrderViewModel: UpdateOrderViewModel
    private lateinit var adapter: ListDetailOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
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
        mainAdminViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainAdminViewModel::class.java]

        detailOrderViewModel = DetailOrderViewModel.getInstance(this)
        updateOrderViewModel = UpdateOrderViewModel.getInstance(this)

        val waiter = getString(R.string.role_waiter)

        mainAdminViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != waiter || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupData() {
        val order = intent.getStringExtra("Order")

        mainAdminViewModel.getUser().observe(this) { user ->
            if (order != null) {
                detailOrderViewModel.getDetailOrder(order, user.token)
            }
        }
    }

    private fun showRecyclerList() {
        val localeID = Locale("in", "ID")
        val nf: NumberFormat = NumberFormat.getInstance(localeID)

        adapter = ListDetailOrderAdapter()

        binding.apply {
            rvOrders.layoutManager = LinearLayoutManager(this@OrderActivity)
            rvOrders.setHasFixedSize(true)
            rvOrders.adapter = adapter
        }

        detailOrderViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailOrderViewModel.order.observe(this) {
            val totalPrice = nf.format(it.totalPrice)

            binding.apply {
                tvOrderId.text = getString(R.string.order_id, it.orderId)
                tvUserId.text = getString(R.string.user_id, it.userId)
                tvItemCode.text = getString(R.string.table_code, it.tableId)
                tvTotalPrice.text = getString(R.string.total_price, totalPrice)
                tvItemStatus.text = getString(R.string.status, it.status)
                btnUpdate.isVisible = true
            }
        }

        detailOrderViewModel.menu.observe(this) {
            adapter.setListOrder(it)
        }

        detailOrderViewModel.error.observe(this) { event ->
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

    private fun updateOrderStatus() {
        val order = intent.getStringExtra("Order")

        mainAdminViewModel.getUser().observe(this) { user ->
            if (order != null) {
                val newStatus = binding.tvUpdateStatus.text.toString()
                updateOrderViewModel.updateStatus(order, newStatus, user.token)
            }
        }

        updateOrderViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (!error) {
                    val alert =
                        SweetAlertDialog(this,
                            SweetAlertDialog.SUCCESS_TYPE)
                    alert.titleText = getString(R.string.success_update)
                    alert.confirmText = getString(R.string.text_ok)
                    alert.contentTextSize = 18
                    alert.setCancelable(false)
                    alert.show()
                    alert.setConfirmClickListener {
                        alert.dismiss()
                        val intent = Intent(this, MainAdminActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val alert =
                        SweetAlertDialog(this,
                            SweetAlertDialog.ERROR_TYPE)
                    alert.titleText = getString(R.string.failed_update)
                    alert.contentText = getString(R.string.network_error)
                    alert.confirmText = getString(R.string.text_ok)
                    alert.contentTextSize = 18
                    alert.setCancelable(false)
                    alert.show()
                }
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            fabHome.setOnClickListener {
                onBackPressed()
            }

            rgStatus.setOnCheckedChangeListener { _, i ->
                val rb: RadioButton = findViewById(i)
                val newStatus = rb.text.toString()

                fabUpdate.isVisible = true
                tvUpdateStatus.text = newStatus
            }

            btnUpdate.setOnClickListener {
                rgStatus.isVisible = true
                btnUpdate.isVisible = false
            }

            fabUpdate.setOnClickListener {
                updateOrderStatus()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}