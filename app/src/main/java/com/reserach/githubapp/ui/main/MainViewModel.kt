package com.reserach.githubapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reserach.githubapp.api.RetrofitClient
import com.reserach.githubapp.data.model.SearchResponse
import com.reserach.githubapp.data.model.User
import com.reserach.githubapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<User>()
    val listUsername = MutableLiveData<ArrayList<SearchResponse>>()
    val errorMessage = MutableLiveData<String>()

    fun setSearchUser(query: String, page: Int) {
        RetrofitClient.apiInstance
            .getUsersBySearch(query,10, page)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    when {
                        response.code() == 200 -> listUsername.postValue(response.body()?.items)
                        response.code() == 401 -> errorMessage.postValue("Unauthorized")
                        response.code() == 403 -> errorMessage.postValue("Forbidden")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getSearchUser() : LiveData<ArrayList<SearchResponse>>{
        return listUsername
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    when {
                        response.code() == 200 -> listUsers.postValue(response.body())
                        response.code() == 401 -> errorMessage.postValue("Unauthorized")
                        response.code() == 403 -> errorMessage.postValue("Forbidden")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUserDetail() : LiveData<User> {
        return listUsers
    }

}