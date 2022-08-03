package com.artevak.kasirpos.data.repository

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.data.service.AuthService
import com.artevak.kasirpos.response.firebase.ResponseProcess

class UserRepository(
    var authService: AuthService
) {
    fun getUsers(response: MutableLiveData<ResponseProcess<Any?>>) = authService.getUsers(response)

    fun login(loginParam: LoginParam, myUser: MutableLiveData<ResponseProcess<User?>>) {
        authService.login(loginParam, myUser)
    }


    fun getMyAccount(response: MutableLiveData<User>) {
        authService.getUser(response)
    }

    fun addUser(user: User, response: MutableLiveData<ResponseProcess<String>>) =
        authService.addUser(user, response)

    fun saveUsername(username: String, password: String) = authService.saveUsername(username, password)

    fun saveUsername(username: String) = authService.saveUsername(username)

    fun getUsername() = authService.getUsername()

    fun savePassword(password: String) = authService.savePassword(password)

    fun getPassword() = authService.getPassword()
    fun updateAccount(user : User) = authService.updateUser(user)
}