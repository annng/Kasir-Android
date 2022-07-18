package com.artevak.kasirpos.ui.activity.profile.view

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.activity.auth.login.LoginActivity
import com.artevak.kasirpos.databinding.ActivityProfileBinding
import com.artevak.kasirpos.data.model.UserPreference

class ProfileActivity : BaseActivity() {
    lateinit var binding : ActivityProfileBinding
    var TAG_LOGOUT = "logout"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        binding.lineLogout.setOnClickListener {
            sendLogoutRequest()
            logout()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
        binding.tvEditProfile.setOnClickListener {

        }

        updateUI()
    }

    fun updateUI(){
        val firstChara = mUserPref.getName()?.take(1)
        binding.tvInisial.setText(firstChara)
        binding.tvName.setText(mUserPref.getName())
        binding.tvEmail.setText(mUserPref.getEmail())
    }

    fun sendLogoutRequest(){
        //TODO logout
    }
}