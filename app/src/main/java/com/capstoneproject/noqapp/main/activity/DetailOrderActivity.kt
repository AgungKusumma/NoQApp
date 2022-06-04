package com.capstoneproject.noqapp.main.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.databinding.ActivityDetailOrderBinding
import com.capstoneproject.noqapp.main.adapter.ListUserOrderAdapter
import com.capstoneproject.noqapp.model.ItemMenu
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var rvItemOrder: RecyclerView
    private val list = ArrayList<ItemMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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

    private fun showRecyclerList() {
        val itemMenu = intent.getParcelableArrayListExtra<ItemMenu>("ItemOrder")

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

    private fun setupAction() {
        binding.btnOrder.setOnClickListener {
            val alert = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            alert.titleText = getString(R.string.order_sucess)
            alert.contentText = getString(R.string.order_text_content)
            alert.confirmText = getString(R.string.text_ok)
            alert.contentTextSize = 18
            alert.setCancelable(false)
            alert.setConfirmClickListener {
                val intent = Intent(this@DetailOrderActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            alert.show()
        }
    }

}