package com.capstoneproject.noqapp.authentication.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.noqapp.R
import com.capstoneproject.noqapp.authentication.viewmodel.AuthenticationViewModel
import com.capstoneproject.noqapp.authentication.viewmodel.SignupViewModel
import com.capstoneproject.noqapp.databinding.ActivitySignupBinding
import com.capstoneproject.noqapp.model.UserModel
import com.capstoneproject.noqapp.model.UserPreference
import com.capstoneproject.noqapp.model.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAccount()
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
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        authenticationViewModel = AuthenticationViewModel.getInstance(this)
    }

    private fun setupAction() {
        binding.apply {
            signupButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                when {
                    name.isEmpty() -> {
                        nameEditText.error = getString(R.string.enter_name)
                    }
                    email.isEmpty() -> {
                        emailEditText.error = getString(R.string.enter_email)
                    }
                    password.isEmpty() -> {
                        passwordEditText.error = getString(R.string.enter_password)
                    }
                    !email.isValidEmail() -> {
                        emailEditTextLayout.error
                    }
                    password.length < 8 -> {
                        passwordEditTextLayout.error
                    }
                    else -> {
                        signupViewModel.saveUser(UserModel(name, email, password, false, "token"))
                        authenticationViewModel.userRegister(name, email, password)
                        authenticationViewModel.error.observe(this@SignupActivity) { event ->
                            event.getContentIfNotHandled()?.let { error ->
                                if (!error) {
                                    Toast.makeText(this@SignupActivity,
                                        getString(R.string.success_create_account),
                                        Toast.LENGTH_LONG).show()
                                    val intent =
                                        Intent(this@SignupActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    AlertDialog.Builder(this@SignupActivity).apply {
                                        setTitle(getString(R.string.register_failed))
                                        setMessage(getString(R.string.error_register_failed))
                                        setPositiveButton(getString(R.string.close)) { _, _ -> }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAccount() {
        binding.loginTextView.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}