package com.capstoneproject.noqapp.main.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.databinding.ActivityDetailOrderBinding
import com.capstoneproject.noqapp.main.adapter.ListUserOrderAdapter
import com.capstoneproject.noqapp.main.viewmodel.DetailOrderViewModel
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var detailOrderViewModel: DetailOrderViewModel
    private lateinit var rvItemOrder: RecyclerView
    private val list = ArrayList<MenuModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
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

        detailOrderViewModel = DetailOrderViewModel.getInstance(this)
    }

    private fun showRecyclerList() {
        val itemMenu = intent.getParcelableArrayListExtra<MenuModel>("ItemOrder")

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
            }
            ("Rp. " + nf.format(subTotalAmount)).also { binding.tvTotalPrice.text = it }
        }
    }

    private fun setupOrder() {
        val tableId = binding.tvTableCode.text.toString()

        val requestBody = RequestOrder(
            tableId = tableId,
            orderItems = list.map {
                OrderItems(
                    menuId = it.menuId,
                    amount = it.totalInCart
                )
            }
        )

        mainViewModel.getUser().observe(this) { user ->
            detailOrderViewModel.orderItem(requestBody, user.token)
        }

        detailOrderViewModel.error.observe(this) { event ->
            event.getContentIfNotHandled()?.let { error ->
                if (!error) {
                    val alert =
                        SweetAlertDialog(this@DetailOrderActivity, SweetAlertDialog.SUCCESS_TYPE)
                    alert.titleText = getString(R.string.order_sucess)
                    alert.contentText = getString(R.string.order_text_content)
                    alert.confirmText = getString(R.string.text_ok)
                    alert.contentTextSize = 18
                    alert.setCancelable(false)
                    alert.show()
                    alert.setConfirmClickListener {
                        alert.dismiss()
                        val intent = Intent(this@DetailOrderActivity, HistoryActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val alert =
                        SweetAlertDialog(this,
                            SweetAlertDialog.ERROR_TYPE)
                    alert.titleText = getString(R.string.order_failed)
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
            imgBtnCode.setOnClickListener {
                if (codeEditText.text.isNullOrEmpty()) {
                    codeEditText.error = getString(R.string.enter_table_code)
                    codeEditText.requestFocus(1)
                } else {
                    tvCode.isVisible = true
                    tvTableCode.isVisible = true
                    codeEditText.error = null
                    codeEditText.clearFocus()
                    it.hideKeyboard()
                    tvTableCode.text = codeEditText.text.toString()
                }
            }

            btnOrder.setOnClickListener {
                val code = tvTableCode.text.toString()
                if (code.isNotEmpty()) {
                    setupOrder()
                } else {
                    val toast = Toast.makeText(this@DetailOrderActivity,
                        getString(R.string.check_button_above),
                        Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    codeEditText.error = getString(R.string.check_button)
                    codeEditText.requestFocus(1)
                }
            }
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}