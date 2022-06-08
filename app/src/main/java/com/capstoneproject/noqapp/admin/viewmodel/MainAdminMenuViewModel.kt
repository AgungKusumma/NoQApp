package com.capstoneproject.noqapp.admin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.noqapp.admin.model.Order
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.utils.Event
import com.capstoneproject.noqapp.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAdminMenuViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _menu = MutableLiveData<ArrayList<Order>>()
    val menu: LiveData<ArrayList<Order>> = _menu

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getOrder(token: String) {
        _isLoading.value = true
        val client = userRepository.getOrder(token)
        client.enqueue(object : Callback<FileUploadResponseAdmin> {
            override fun onResponse(
                call: Call<FileUploadResponseAdmin>,
                response: Response<FileUploadResponseAdmin>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userResponse = response.body()?.data
                    userRepository.appExecutors.networkIO.execute {
                        _menu.postValue(userResponse!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<FileUploadResponseAdmin>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "MainAdminMenuViewModel"

        @Volatile
        private var instance: MainAdminMenuViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): MainAdminMenuViewModel =
            instance ?: synchronized(this) {
                instance ?: MainAdminMenuViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}