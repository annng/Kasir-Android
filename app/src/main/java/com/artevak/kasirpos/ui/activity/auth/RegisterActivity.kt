package com.artevak.kasirpos.ui.activity.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityRegisterBinding
import com.artevak.kasirpos.model.RegisterInfo
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.ui.activity.HomeActivity
import com.artevak.kasirpos.util.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val name = binding.etNamaPemilik.text.toString()
        val phone = binding.etPhone.text.toString()
        val email = binding.etEmail.text.toString()
        val nama_umkm = binding.etNamaUmkm.text.toString()
        val alamat = binding.etAlamat.text.toString()
        val password = binding.etPassword.text.toString()
        val confirm_password = binding.etCPassword.text.toString()

        if (name.equals("") || name.length == 0){
            showErrorMessage("Nama Belum diisi")
        }else  if (phone.equals("") || phone.length == 0){
            showErrorMessage("Telepon Belum diisi")
        }else if (email.equals("") || email.length == 0){
            showErrorMessage("Email Belum diisi")
        }else if (nama_umkm.equals("") || nama_umkm.length == 0){
            showErrorMessage("Nama UMKM Belum diisi")
        }else if (alamat.equals("") || alamat.length == 0){
            showErrorMessage("Alamat Belum diisi")
        }else if (password.equals("") || password.length == 0){
            showErrorMessage("Password Belum diisi")
        }else if (confirm_password.equals("") || confirm_password.length == 0){
            showErrorMessage("Konfirmasi Password Belum diisi")
        }else if (!password.equals(confirm_password)){
            showErrorMessage("Konfirmasi password tidak valid")
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