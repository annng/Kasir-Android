package com.artevak.kasirpos.ui.activity.auth.login

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.databinding.ActivityLoginBinding
import com.artevak.kasirpos.data.model.LoginInfo
import com.artevak.kasirpos.data.model.UserPreference
import com.artevak.kasirpos.ui.activity.splash.SplashActivity
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.activity.auth.register.RegisterActivity

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
        binding.btnRegister.setOnClickListener {
            gotoRegister()
        }
    }

    fun checkValidation(){
        val email = binding.etEmail.text
        val password = binding.etPassword.text

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

    fun gotoRegister(){
        val i = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(i)

    }
}