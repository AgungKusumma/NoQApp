package com.capstoneproject.noqapp.admin.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.model.AddMenuModel
import com.capstoneproject.noqapp.admin.viewmodel.AddMenuViewModel
import com.capstoneproject.noqapp.admin.viewmodel.MainAdminViewModel
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivityAddMenuBinding
import com.capstoneproject.noqapp.model.*

class AddMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMenuBinding
    private lateinit var mainAdminViewModel: MainAdminViewModel
    private lateinit var addMenuViewModel: AddMenuViewModel
    private val timeInterval = 2000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupNewMenu()
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

        addMenuViewModel = AddMenuViewModel.getInstance(this)

        val admin = getString(R.string.role_admin)

        mainAdminViewModel.getUser().observe(this) { user ->
            if (!user.isLogin || user.role != admin || user.token.isEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupNewMenu() {
        binding.apply {
            uploadButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val desc = descEditText.text.toString()
                val priceET = priceEditText.text.toString()
                val img = photoUrlEditText.text.toString()

                when {
                    name.isEmpty() -> {
                        nameEditText.error = getString(R.string.enter_menu_name)
                        nameEditText.requestFocus(1)
                    }
                    desc.isEmpty() -> {
                        descEditText.error = getString(R.string.enter_desc)
                        descEditText.requestFocus(1)
                    }
                    priceET.isEmpty() -> {
                        priceEditText.error = getString(R.string.enter_price)
                        priceEditText.requestFocus(1)
                    }
                    img.isEmpty() -> {
                        photoUrlEditText.error = getString(R.string.enter_photo_url)
                        photoUrlEditText.requestFocus(1)
                    }
                    else -> {
                        it.hideKeyboard()
                        val price = Integer.parseInt(priceET)
                        val menu = AddMenuModel(
                            name = name,
                            description = desc,
                            price = price,
                            photoUrl = img
                        )

                        mainAdminViewModel.getUser().observe(this@AddMenuActivity) { user ->
                            addMenuViewModel.addMenu(menu, user.token)
                        }
                        addMenuViewModel.error.observe(this@AddMenuActivity) { event ->
                            event.getContentIfNotHandled()?.let { error ->
                                if (!error) {
                                    val alert =
                                        SweetAlertDialog(this@AddMenuActivity,
                                            SweetAlertDialog.SUCCESS_TYPE)
                                    alert.titleText = getString(R.string.upload_success)
                                    alert.contentText = getString(R.string.menu_added)
                                    alert.confirmText = getString(R.string.text_ok)
                                    alert.contentTextSize = 18
                                    alert.setCancelable(false)
                                    alert.show()
                                    alert.setConfirmClickListener {
                                        alert.dismiss()
                                        nameEditText.text = null
                                        descEditText.text = null
                                        priceEditText.text = null
                                        photoUrlEditText.text = null
                                        nameEditText.clearFocus()
                                        descEditText.clearFocus()
                                        priceEditText.clearFocus()
                                        photoUrlEditText.clearFocus()
                                    }
                                } else {
                                    val alert =
                                        SweetAlertDialog(this@AddMenuActivity,
                                            SweetAlertDialog.ERROR_TYPE)
                                    alert.titleText = getString(R.string.upload_failed)
                                    alert.contentText = getString(R.string.network_error)
                                    alert.confirmText = getString(R.string.text_ok)
                                    alert.contentTextSize = 18
                                    alert.setCancelable(false)
                                    alert.show()
                                }
                            }
                        }
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
                Toast.makeText(this@AddMenuActivity,
                    getString(R.string.logout_success), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
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