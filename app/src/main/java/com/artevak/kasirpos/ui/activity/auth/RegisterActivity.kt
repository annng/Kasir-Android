package com.artevak.kasirpos.ui.activity.auth

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityRegisterBinding
import com.artevak.kasirpos.model.RegisterInfo
import com.artevak.kasirpos.ui.activity.HomeActivity

class RegisterActivity : BaseActivity() {
    lateinit var binding : ActivityRegisterBinding
    lateinit var registerInfo : RegisterInfo
    var TAG_DAFTAR = "daftar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnDaftar.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        val name = binding.etNamaPemilik.text
        val email = binding.etEmail.text
        val nama_umkm = binding.etNamaUmkm.text
        val password = binding.etPassword.text
        val confirm_password = binding.etCPassword.text


        if (name.isEmpty()){
            binding.etNamaPemilik.setInfoError(getString(R.string.error_field_required))
        }else if (email.isEmpty()){
            binding.etEmail.setInfoError(getString(R.string.error_field_required))
        }else if (nama_umkm.isEmpty()){
            binding.etNamaUmkm.setInfoError(getString(R.string.error_field_required))
        }else if (password.isEmpty()){
            binding.etPassword.setInfoError(getString(R.string.error_field_required))
        }else if (confirm_password.isEmpty()){
            binding.etCPassword.setInfoError(getString(R.string.error_field_required))
        }else if (password != confirm_password){
            binding.etNamaPemilik.setInfoError(getString(R.string.error_field_password_not_match))
        }else {
            //TODO register process
            gotoMainPage()
        }
    }

    private fun gotoMainPage(){
        val i = Intent(this@RegisterActivity, HomeActivity::class.java)
        startActivity(i)
    }
}