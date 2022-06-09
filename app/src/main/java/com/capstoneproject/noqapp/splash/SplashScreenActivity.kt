package com.capstoneproject.noqapp.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.admin.activity.MainAdminActivity
import com.capstoneproject.noqapp.authentication.activity.LoginActivity
import com.capstoneproject.noqapp.databinding.ActivitySplashScreenBinding
import com.capstoneproject.noqapp.main.activity.MainActivity
import com.capstoneproject.noqapp.main.activity.dataStore
import com.capstoneproject.noqapp.main.viewmodel.MainViewModel
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            setupViewModel()
        }, delayTime)//delay 3 seconds
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

        val customer = getString(R.string.role_customer)
        val waiter = getString(R.string.role_waiter)

        mainViewModel.getUser().observe(this) { user ->
            when {
                user.isLogin && user.role == waiter -> {
                    startActivity(Intent(this, MainAdminActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                user.isLogin && user.role == customer -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val delayTime: Long = 3000
    }
}