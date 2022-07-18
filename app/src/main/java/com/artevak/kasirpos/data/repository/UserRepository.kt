package com.artevak.kasirpos.data.repository

import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.data.service.UserService
import com.google.firebase.database.FirebaseDatabase

class UserRepository(
    var userService : UserService,
    var sharedPref: SharedPref
) {

}