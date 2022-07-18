package com.artevak.kasirpos.data.service

import android.content.Context
import com.artevak.kasirpos.common.const.DBConst
import com.google.firebase.database.FirebaseDatabase

class UserService(val context : Context) {
    var db : FirebaseDatabase = FirebaseDatabase.getInstance(DBConst.DB.URL)
}