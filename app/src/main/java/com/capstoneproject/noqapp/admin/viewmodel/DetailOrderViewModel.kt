package com.capstoneproject.noqapp.admin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.noqapp.admin.model.DetailOrderModel
import com.capstoneproject.noqapp.api.FileUploadResponseAdmin
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.utils.Event
import com.capstoneproject.noqapp.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailOrderViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _order = MutableLiveData<FileUploadResponseAdmin>()
    val order: LiveData<FileUploadResponseAdmin> = _order

    private var _menu = MutableLiveData<ArrayList<DetailOrderModel>>()
    val menu: LiveData<ArrayList<DetailOrderModel>> = _menu

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailOrder(orderId: String, token: String) {
        _isLoading.value = true
        val client = userRepository.getDetailOrder(orderId, token)
        client.enqueue(object : Callback<FileUploadResponseAdmin> {
            override fun onResponse(
                call: Call<FileUploadResponseAdmin>,
                response: Response<FileUploadResponseAdmin>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userResponse = response.body()?.orderItems
                    userRepository.appExecutors.networkIO.execute {
                        _menu.postValue(userResponse!!)
                        _order.postValue(response.body())
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