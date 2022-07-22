package com.artevak.kasirpos.ui.activity.auth.login

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.R
import com.artevak.kasirpos.databinding.ActivityLoginBinding
import com.artevak.kasirpos.data.model.LoginInfo
import com.artevak.kasirpos.ui.activity.splash.SplashActivity
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.data.model.param.LoginParam
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.auth.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    lateinit var binding  : ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModel()
    lateinit var loginInfo: LoginInfo
    var TAG_LOGIN = "login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            checkValidation()
        }
        binding.btnRegister.setOnClickListener {
            gotoRegister()
        }

        observeData()
    }

    private fun observeData(){
        viewModel.user.observe(this){
            when(it.status){
                StatusRequest.SUCCESS -> {
                    it.data?.username?.let { it1 -> viewModel.saveSession(it1) }
                    gotoSplashScreen()
                }
                else -> {
                    it.message?.let { it1 -> showErrorMessage(it1) }
                }
            }
        }
    }

    fun checkValidation(){
        val username = binding.etUsername.text
        val password = binding.etPassword.text

        if (username.isEmpty()){
            binding.etUsername.setInfoError(getString(R.string.error_field_required))
        }else if (password.isEmpty()){
            binding.etPassword.setInfoError(getString(R.string.error_field_required))
        }else {
            val loginParam = LoginParam(username, password)
            viewModel.login(loginParam)
        }
    }

    fun gotoSplashScreen(){
        val i = Intent(this@LoginActivity, SplashActivity::class.java)
        startActivity(i)
        finish()
    }

    fun gotoRegister(){
        val i = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(i)

    }
}