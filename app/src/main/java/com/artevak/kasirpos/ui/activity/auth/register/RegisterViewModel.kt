package com.artevak.kasirpos.ui.activity.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.response.firebase.ResponseProcess

class RegisterViewModel(val registerUseCase: RegisterUseCase) : BaseViewModel() {
    private val _responseAdd = MutableLiveData<ResponseProcess<String>>()
    val responseAdd : LiveData<ResponseProcess<String>> = _responseAdd
    fun addUser(user : User){
        registerUseCase.addUser(user, _responseAdd)
    }

    fun saveUsername(username : String) = registerUseCase.saveUsername(username)
}