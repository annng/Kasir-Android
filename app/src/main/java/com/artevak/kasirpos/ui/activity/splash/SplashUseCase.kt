package com.artevak.kasirpos.ui.activity.splash

import com.artevak.kasirpos.data.repository.UserRepository

class SplashUseCase(private val userRepo : UserRepository) {
    fun getUsername() = userRepo.getUsername()
}