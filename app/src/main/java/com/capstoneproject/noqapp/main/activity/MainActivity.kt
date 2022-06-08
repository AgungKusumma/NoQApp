package com.capstoneproject.noqapp.main.activity

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
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityMainBinding
import com.capstoneproject.noqapp.main.adapter.ListMenuAdapter
import com.capstoneproject.noqapp.main.viewmodel.MainMenuViewModel
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.MenuModel
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), ListMenuAdapter.MenuList {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainMenuViewModel: MainMenuViewModel
    private lateinit var adapter: ListMenuAdapter
    private val listOrder = ArrayList<MenuModel>()
    private var itemsInCart: MutableList<MenuModel>? = null
    private var itemCount = 0
    private val timeInterval = 2000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupViewModel()
        setupListMenu()
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
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainMenuViewModel = MainMenuViewModel.getInstance(this)

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupListMenu() {
        mainViewModel.getUser().observe(this) { user ->
            mainMenuViewModel.getMenu(user.token)
        }
    }

    private fun showRecyclerList() {
        adapter = ListMenuAdapter(this)

        binding.apply {
            rvItemMenu.layoutManager = GridLayoutManager(this@MainActivity, 2)
            rvItemMenu.setHasFixedSize(true)
            rvItemMenu.adapter = adapter
        }

        mainMenuViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainMenuViewModel.menu.observe(this) {
            adapter.setListMenu(it)
        }

        mainMenuViewModel.message.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                binding.apply {
                    ivEmpty.isVisible = true
                    progressBar.isVisible = false
                    rvItemMenu.adapter = null
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnOrder.setOnClickListener {
            itemsInCart?.let { listOrder.addAll(it) }

            val intent = Intent(this@MainActivity, DetailOrderActivity::class.java)
            intent.putExtra("ItemOrder", listOrder)
            startActivity(intent)

            listOrder.clear()
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
                Toast.makeText(this@MainActivity,
                    getString(R.string.logout_success), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun addMenu(menu: MenuModel) {
        if (itemsInCart.isNullOrEmpty()) {
            itemsInCart = ArrayList()
        }
        itemsInCart?.add(menu)
        itemCount = 0
        itemsInCart?.forEach { item ->
            itemCount += item.totalInCart
        }
        binding.btnOrder.isVisible = true
        "Order $itemCount Items".also { binding.btnOrder.text = it }
    }

    override fun updateMenu(menu: MenuModel) {
        val index = itemsInCart?.indexOf(menu)
        if (index != null) {
            itemsInCart?.removeAt(index)
        }
        itemsInCart?.add(menu)
        itemCount = 0
        itemsInCart?.forEach { item ->
            itemCount += item.totalInCart
        }
        "Order $itemCount Items".also { binding.btnOrder.text = it }
    }

    override fun removeMenu(menu: MenuModel) {
        if (itemsInCart?.contains(menu) == true) {
            itemsInCart?.remove(menu)
            itemCount = 0
            itemsInCart?.forEach { item ->
                itemCount += item.totalInCart
            }
            if (itemCount == 0) {
                binding.btnOrder.isVisible = false
            }
            "Order $itemCount Items".also { binding.btnOrder.text = it }
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