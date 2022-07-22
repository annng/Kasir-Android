package com.artevak.kasirpos.data.model.shared

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by crocodic7 on 07/09/18.
 */
@SuppressLint("CommitPrefEdits")
open class SharedPref constructor(context: Context) {
    // Shared Preferences
    var pref: SharedPreferences

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor

    // Context
    var _context: Context

    // Shared pref mode
    internal var PRIVATE_MODE = 0

    // nama sharepreference
    private val PREF_NAME = "eCashier"

    // All Shared Preferences Keys
    private val KEY_USERNAME = "username"
    private val KEY_PASSWORD = "password"

    private val IS_LOGIN = "is_login"


    // Constructor
    init {
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun isLogged(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        setIsLogin(false)
        setUsername("")

    }


    fun setIsLogin(isLogin: Boolean) {
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.commit()

        if (!isLogin) {

        }
    }

    fun getUsername(): String {
        return pref.getString(KEY_USERNAME, "") ?: ""
    }

    fun getPassword(): String {
        return pref.getString(KEY_PASSWORD, "") ?: ""
    }

    fun setUsername(username: String) {
        editor.putString(KEY_USERNAME, username)
        editor.commit()
    }

    fun setPassword(password: String) {
        editor.putString(KEY_PASSWORD, password)
        editor.commit()
    }
}