package com.artevak.kasirpos.ui.activity.profile.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.response.firebase.ResponseData

class ProfileViewModel(private val useCase: ProfileUseCase) : BaseViewModel() {
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    fun getUser(){
        Log.e("request", "jalan")
        useCase.getMyAccount(_user)
    }
}