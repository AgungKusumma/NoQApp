package com.capstoneproject.noqapp.main.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityMainBinding
import com.capstoneproject.noqapp.main.adapter.ListMenuAdapter
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.ItemMenu
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), ListMenuAdapter.MenuList {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvItemMenu: RecyclerView
    private var list = ArrayList<ItemMenu>()
    private var itemsInCart: MutableList<ItemMenu>? = null
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

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun showRecyclerList() {
        rvItemMenu = binding.rvItemMenu
        rvItemMenu.setHasFixedSize(true)

        list.addAll(listMenu)

        rvItemMenu.layoutManager = GridLayoutManager(this, 2)

        val listMenuAdapter = ListMenuAdapter(list, this)
        rvItemMenu.adapter = listMenuAdapter

    }

    private val listMenu: ArrayList<ItemMenu>
        get() {
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataName = resources.getStringArray(R.array.data_name)
            val dataPrice = resources.getIntArray(R.array.data_price)
            val listMenu = ArrayList<ItemMenu>()
            for (i in dataName.indices) {
                val menu = ItemMenu(dataPhoto[i], dataName[i], dataPrice[i])
                listMenu.add(menu)
            }
            return listMenu
        }

    private fun setupAction() {
        val listOrder = ArrayList<ItemMenu>()

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
            alert.setConfirmClickListener {
                mainViewModel.logout()
                Toast.makeText(this@MainActivity,
                    getString(R.string.logout_success), Toast.LENGTH_LONG).show()
            }
            alert.show()

        }
    }

    override fun addMenu(menu: ItemMenu) {
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

    override fun updateMenu(menu: ItemMenu) {
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

    override fun removeMenu(menu: ItemMenu) {
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