package com.artevak.kasirpos.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.google.firebase.database.FirebaseDatabase

open class BaseViewModel: ViewModel() {
    lateinit var db : FirebaseDatabase
    lateinit var sharedPref : SharedPref
}