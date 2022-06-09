package com.capstoneproject.noqapp.admin.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.adapter.ListOrderAdapter
import com.capstoneproject.noqapp.admin.viewmodel.MainAdminMenuViewModel
import com.capstoneproject.noqapp.admin.viewmodel.MainAdminViewModel
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityMainAdminBinding
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var mainAdminMenuViewModel: MainAdminMenuViewModel
    private lateinit var adapter: ListOrderAdapter
    private val timeInterval = 2000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
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
        mainAdminViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainAdminViewModel::class.java]

        mainAdminMenuViewModel = MainAdminMenuViewModel.getInstance(this)

        val waiter = getString(R.string.role_waiter)

        mainAdminViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != waiter || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupListOrder() {
        mainAdminViewModel.getUser().observe(this) { user ->
            mainAdminMenuViewModel.getOrder(user.token)
        }
    }

    private fun showRecyclerList() {
        adapter = ListOrderAdapter()

        binding.apply {
            rvOrders.layoutManager = LinearLayoutManager(this@MainAdminActivity)
            rvOrders.setHasFixedSize(true)
            rvOrders.adapter = adapter
        }

        mainAdminMenuViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainAdminMenuViewModel.menu.observe(this) {
            adapter.setListOrder(it)
        }

        mainAdminMenuViewModel.error.observe(this) { event ->
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
                mainAdminViewModel.logout()
                Toast.makeText(this@MainAdminActivity,
                    getString(R.string.logout_success), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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