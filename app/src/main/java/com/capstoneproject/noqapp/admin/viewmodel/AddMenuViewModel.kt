package com.capstoneproject.noqapp.admin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.noqapp.admin.model.AddMenuModel
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.utils.Event
import com.capstoneproject.noqapp.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMenuViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun addMenu(menu: AddMenuModel, token: String) {
        val client = userRepository.addMenu(menu, token)
        client.enqueue(object : Callback<FileUploadResponseAdmin> {
            override fun onResponse(
                call: Call<FileUploadResponseAdmin>,
                response: Response<FileUploadResponseAdmin>,
            ) {
                Log.d(TAG, "onResponse: " + response.body())
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    Log.d(TAG, "onResponse Success: ${response.message()}")
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<FileUploadResponseAdmin>, t: Throwable) {
                Log.e(TAG, "onFailure: " + t.message)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    companion object {
        private const val TAG = "AddMenuViewModel"

        @Volatile
        private var instance: AddMenuViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): AddMenuViewModel =
            instance ?: synchronized(this) {
                instance ?: AddMenuViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}