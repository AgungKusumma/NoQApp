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

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveData(token: String?, name: String?) {
        viewModelScope.launch {
            if (token != null && name != null) {
                pref.saveData(token, name)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}