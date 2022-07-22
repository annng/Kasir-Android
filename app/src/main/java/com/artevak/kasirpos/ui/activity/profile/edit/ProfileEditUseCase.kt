package com.artevak.kasirpos.ui.activity.profile.edit

import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.repository.UserRepository

class ProfileEditUseCase(private val userRepository: UserRepository) {
    fun getAccount(user: MutableLiveData<User>) = userRepository.getMyAccount(user)
    fun getPassword() = userRepository.getPassword()
    fun updateAccount(user : User) = userRepository.updateAccount(user)
}