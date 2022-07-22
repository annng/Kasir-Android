package com.artevak.kasirpos.ui.activity.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.response.firebase.ResponseProcess

class LoginViewModel(val useCase : LoginUseCase) : BaseViewModel() {
    private val _user = MutableLiveData<ResponseProcess<User?>>()
    val user : LiveData<ResponseProcess<User?>> = _user

    fun login(loginParam : LoginParam){
        useCase.login(loginParam, _user)
    }

    fun saveSession(username : String, password : String){
        useCase.saveSession(username, password)
    }
}