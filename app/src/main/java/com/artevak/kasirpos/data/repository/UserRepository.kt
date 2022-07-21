package com.artevak.kasirpos.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.common.util.ext.isExist
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.service.AuthService
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess

class UserRepository(
    var authService: AuthService,
    var sharedPref: SharedPref
) {
    fun getUsers(response: MutableLiveData<ResponseProcess<Any?>>) = authService.getUsers(response)

    fun login(loginParam: LoginParam, myUser: MutableLiveData<ResponseProcess<User?>>) {
        authService.login(loginParam, myUser)
    }


    fun getMyAccount(response: MutableLiveData<User>) {
        authService.getUser(sharedPref.getUsername(), response)
    }

    fun addUser(user: User, response: MutableLiveData<ResponseProcess<String>>) =
        authService.addUser(user, response)

    fun saveUsername(username: String) {
        sharedPref.setUsername(username)
    }

    fun getUsername() = sharedPref.getUsername()
}