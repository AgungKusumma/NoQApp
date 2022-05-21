package com.capstoneproject.noqapp.authentication.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.ViewModelFactory
import com.capstoneproject.noqapp.admin.activity.MainAdminActivity
import com.capstoneproject.noqapp.authentication.viewmodel.LoginViewModel
import com.capstoneproject.noqapp.databinding.ActivityLoginBinding
import com.capstoneproject.noqapp.model.UserModel
import com.capstoneproject.noqapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAccount()
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                when {
                    email.isEmpty() -> {
                        emailEditText.error = getString(R.string.enter_email)
                    }
                    password.isEmpty() -> {
                        passwordEditText.error = getString(R.string.enter_password)
                    }
                    email != user.email || password != user.password -> {
                        emailEditText.error = getString(R.string.wrong_password_and_email)
                        passwordEditText.error = getString(R.string.wrong_password_and_email)

                        Toast.makeText(this@LoginActivity,
                            getString(R.string.login_failed), Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        loginViewModel.login()
                        Toast.makeText(this@LoginActivity,
                            getString(R.string.login_success),
                            Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginActivity, MainAdminActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun setupAccount() {
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}