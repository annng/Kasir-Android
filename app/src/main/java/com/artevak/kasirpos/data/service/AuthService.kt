package com.artevak.kasirpos.data.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.R
import com.artevak.kasirpos.common.const.DBConst
import com.artevak.kasirpos.common.util.ext.*
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AuthService(val context: Context, val db: FirebaseDatabase, val sharedPref : SharedPref) {
    val userRef = db.getReference(DBConst.TABLE.USER)

    fun getUsers(response: MutableLiveData<ResponseProcess<Any?>>) {
        response.postValue(ResponseProcess(null, StatusRequest.LOADING))
        db.getAllData<User>(DBConst.TABLE.USER, {
            response.postValue(ResponseProcess(it, StatusRequest.SUCCESS))
        }, {
            response.postValue(ResponseProcess(null, StatusRequest.FAILED))
        })

    }

    fun login(loginParam: LoginParam, response: MutableLiveData<ResponseProcess<User?>>) {
        response.postValue(ResponseProcess(null, StatusRequest.LOADING))
        userRef.child(loginParam.username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if (user.password == loginParam.password) {
                        response.postValue(ResponseProcess(user, StatusRequest.SUCCESS))
                    } else {
                        response.postValue(
                            ResponseProcess(
                                null, StatusRequest.FAILED, context.getString(
                                    R.string.error_field_password_not_match
                                )
                            )
                        )
                    }
                } else {
                    response.postValue(
                        ResponseProcess(
                            null, StatusRequest.FAILED, context.getString(
                                R.string.error_toast_user_not_found
                            )
                        )
                    )
                }
            }


            override fun onCancelled(error: DatabaseError) {
                response.postValue(ResponseProcess(null, StatusRequest.FAILED))
            }

        })
    }

    fun getUser(response: MutableLiveData<User>) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.map { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let {
                        if (it.username == sharedPref.getUsername()) {
                            response.postValue(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun addUser(user: User, response: MutableLiveData<ResponseProcess<String>>) {
        response.postValue(
            ResponseProcess(
                context.getString(R.string.info_toast_loading),
                StatusRequest.LOADING
            )
        )
        userRef.child(user.username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toInt() == 0) {
                    response.postValue(ResponseProcess("Success", StatusRequest.SUCCESS))
                    db.addCustomKey(DBConst.TABLE.USER, user.username, user) {

                    }
                } else {
                    response.postValue(
                        ResponseProcess(
                            context.getString(R.string.error_toast_username_exist),
                            StatusRequest.FAILED
                        )
                    )
                }

            }

            override fun onCancelled(error: DatabaseError) {
                response.postValue(
                    ResponseProcess(
                        context.getString(R.string.error_field_required),
                        StatusRequest.ERROR
                    )
                )
            }

        })

    }

    fun updateUser(user: User) {
        db.updateData(DBConst.TABLE.USER, user.username, user)
    }

    fun saveUsername(username: String, password: String) {
        sharedPref.setUsername(username)
        sharedPref.setPassword(password)
    }

    fun saveUsername(username: String) {
        sharedPref.setUsername(username)
    }

    fun getUsername() = sharedPref.getUsername()

    fun savePassword(password: String) {
        sharedPref.setPassword(password)
    }

    fun getPassword() = sharedPref.getPassword()

}