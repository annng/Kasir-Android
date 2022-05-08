package com.tapisdev.penjualankasir.model

import android.content.Context

class UserPreference(context: Context) {
    private val KEY_NAME = "name"
    private val KEY_USERNAME = "username"
    private val KEY_JENIS_USER = "jenis_user"
    private val KEY_EMAIL = "email"
    private val KEY_PHONE = "phone"
    private val KEY_AVATAR = "avatar"
    private val KEY_NIK = "nik"
    private val KEY_TOKEN = "token"
    private val KEY_FCM_TOKEN = "fcm_token"

    var PREFS_NAME = "UserPref"
    private val preferences  = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun saveName(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_NAME,text)
        editor.commit()
    }

    fun saveUsername(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_USERNAME,text)
        editor.commit()
    }

    fun saveJenisUser(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_JENIS_USER,text)
        editor.commit()
    }

    fun saveEmail(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_EMAIL,text)
        editor.commit()
    }

    fun savePhone(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_PHONE,text)
        editor.commit()
    }

    fun saveAvatar(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_AVATAR,text)
        editor.commit()
    }

    fun saveNIK(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_NIK,text)
        editor.commit()
    }

    fun saveToken(text : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_TOKEN,text)
        editor.commit()
    }

    fun saveFCMToken(fcm_token : String){
        val editor = preferences!!.edit()
        editor.putString(KEY_FCM_TOKEN,fcm_token)
        editor.commit()
    }

    fun getName() : String?{
        return preferences!!.getString(KEY_NAME,null)
    }

    fun getUsername() : String?{
        return preferences!!.getString(KEY_USERNAME,null)
    }

    fun getJenisUser() : String?{
        var jenisUser = preferences?.getString(KEY_JENIS_USER,"none")
        return jenisUser
    }

    fun getEmail() : String?{
        return preferences!!.getString(KEY_EMAIL,null)
    }

    fun getPhone() : String?{
        return preferences!!.getString(KEY_PHONE,null)
    }

    fun getAvatar() : String?{
        return preferences!!.getString(KEY_AVATAR,null)
    }

    fun getNIK() : String?{
        return preferences!!.getString(KEY_NIK,null)
    }

    fun getToken() : String?{
        return preferences!!.getString(KEY_TOKEN,null)
    }

    fun getFCMToken() : String?{
        return preferences!!.getString(KEY_FCM_TOKEN,null)
    }



}