package com.capstoneproject.noqapp.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.model.RequestOrder
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.utils.Event
import com.capstoneproject.noqapp.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailOrderViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun orderItem(requestOrder: RequestOrder, token: String) {
        val client = userRepository.orderItem(requestOrder, token)
        client.enqueue(object : Callback<FileUploadResponseAdmin> {
            override fun onResponse(
                call: Call<FileUploadResponseAdmin>,
                response: Response<FileUploadResponseAdmin>,
            ) {
                userRepository.appExecutors.networkIO.execute {
                    if (response.isSuccessful) {
                        _error.postValue(Event(false))
                    } else {
                        Log.e(TAG, "onResponse fail: ${response.message()}")
                        _error.postValue(Event(true))
                        _message.postValue(Event(response.message()))
                    }
                }
            }

            override fun onFailure(
                call: Call<FileUploadResponseAdmin>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _error.postValue(Event(true))
                _message.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "DetailOrderViewModel"

        @Volatile
        private var instance: DetailOrderViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): DetailOrderViewModel =
            instance ?: synchronized(this) {
                instance ?: DetailOrderViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}