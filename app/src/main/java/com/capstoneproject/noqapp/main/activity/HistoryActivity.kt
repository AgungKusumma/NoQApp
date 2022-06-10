package com.capstoneproject.noqapp.main.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityHistoryBinding
import com.capstoneproject.noqapp.main.adapter.ListOrderHistoryAdapter
import com.capstoneproject.noqapp.main.viewmodel.HistoryViewModel
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var adapter: ListOrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupListOrder()
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

        historyViewModel = HistoryViewModel.getInstance(this)

        val customer = getString(R.string.role_customer)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.token.isEmpty() || user.role != customer) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupListOrder() {
        mainViewModel.getUser().observe(this) { user ->
            historyViewModel.getOrderHistory(user.token)
        }
    }

    private fun showRecyclerList() {
        adapter = ListOrderHistoryAdapter()

        binding.apply {
            rvOrders.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvOrders.setHasFixedSize(true)
            rvOrders.adapter = adapter
        }

        historyViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        historyViewModel.menu.observe(this) {
            binding.ivNoHistory.isVisible = it.isEmpty()
            adapter.setListOrder(it)
        }

        historyViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (error) {
                    binding.apply {
                        ivEmpty.isVisible = true
                        progressBar.isVisible = false
                        ivNoHistory.isVisible = false
                        rvOrders.adapter = null
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.fabHome.setOnClickListener {
            val intent = Intent(this@HistoryActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.fabLogout.setOnClickListener {
            val alert =
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            alert.titleText = getString(R.string.logout)
            alert.contentText = getString(R.string.text_logout)
            alert.confirmText = getString(R.string.yes)
            alert.cancelText = getString(R.string.no)
            alert.contentTextSize = 18
            alert.setCancelable(false)
            alert.show()
            alert.setConfirmClickListener {
                alert.dismiss()
                mainViewModel.logout()
                Toast.makeText(this@HistoryActivity,
                    getString(R.string.logout_success), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}