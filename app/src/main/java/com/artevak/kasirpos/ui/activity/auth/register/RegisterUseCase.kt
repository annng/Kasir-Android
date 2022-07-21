package com.artevak.kasirpos.ui.activity.auth.register

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.repository.UserRepository
import com.artevak.kasirpos.response.firebase.ResponseProcess

class RegisterUseCase (private val userRepository: UserRepository){
    fun addUser(user: User, response : MutableLiveData<ResponseProcess<String>>) = userRepository.addUser(user, response)
    fun saveUsername(username :String) = userRepository.saveUsername(username)
}