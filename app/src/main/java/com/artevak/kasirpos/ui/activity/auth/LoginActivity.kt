package com.artevak.kasirpos.ui.activity.auth

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.databinding.ActivityLoginBinding
import com.artevak.kasirpos.model.LoginInfo
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.ui.activity.splash.SplashActivity
import com.artevak.kasirpos.base.BaseActivity

class LoginActivity : BaseActivity() {
    lateinit var binding  : ActivityLoginBinding
    lateinit var loginInfo: LoginInfo
    var TAG_LOGIN = "login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        binding.btnLogin.setOnClickListener {
            checkValidation()
        }
        binding.tvKeRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    fun checkValidation(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.equals("") || email.length == 0){
            showErrorMessage("Email Belum diisi")
        }else if (password.equals("") || password.length == 0){
            showErrorMessage("Password Belum diisi")
        }else {
            //TODO signin process
            gotoSplashScreen()
        }
    }

    fun gotoSplashScreen(){
        val i = Intent(this@LoginActivity, SplashActivity::class.java)
        startActivity(i)

    }
}