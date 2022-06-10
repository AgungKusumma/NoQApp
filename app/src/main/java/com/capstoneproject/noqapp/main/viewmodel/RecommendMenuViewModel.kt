package com.capstoneproject.noqapp.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.noqapp.api.FileUploadResponse
import com.capstoneproject.noqapp.model.MenuModel
import com.capstoneproject.noqapp.model.UserRepository
import com.capstoneproject.noqapp.utils.Event
import com.capstoneproject.noqapp.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendMenuViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _menu = MutableLiveData<ArrayList<MenuModel>>()
    val menu: LiveData<ArrayList<MenuModel>> = _menu

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRecommend(token: String) {
        _isLoading.value = true
        val client = userRepository.getRecommend(token)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _error.postValue(Event(false))
                    val userResponse = response.body()?.data
                    userRepository.appExecutors.networkIO.execute {
                        _menu.postValue(userResponse!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _error.postValue(Event(true))
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<FileUploadResponse>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _error.postValue(Event(true))
                _message.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "RecommendMenuViewModel"

        @Volatile
        private var instance: RecommendMenuViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): RecommendMenuViewModel =
            instance ?: synchronized(this) {
                instance ?: RecommendMenuViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}