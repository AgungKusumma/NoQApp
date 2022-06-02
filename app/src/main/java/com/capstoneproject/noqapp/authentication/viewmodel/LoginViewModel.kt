package com.capstoneproject.noqapp.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstoneproject.noqapp.model.UserModel
import com.capstoneproject.noqapp.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveData(token: String?, admin: Boolean?) {
        viewModelScope.launch {
            if (token != null && admin != null) {
                pref.saveData(token, admin)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}