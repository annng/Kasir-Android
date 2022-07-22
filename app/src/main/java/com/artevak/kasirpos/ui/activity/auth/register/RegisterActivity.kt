package com.artevak.kasirpos.ui.activity.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityRegisterBinding
import com.artevak.kasirpos.data.model.RegisterInfo
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var registerInfo: RegisterInfo
    var TAG_DAFTAR = "daftar"

    var user: User? = null

    private val viewModel: RegisterViewModel by viewModel()

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

        observeData()
    }

    fun checkValidation() {
        val name = binding.etNamaPemilik.text
        val username = binding.etUsername.text
        val nama_umkm = binding.etNamaUmkm.text
        val password = binding.etPassword.text
        val confirm_password = binding.etCPassword.text


        if (name.isEmpty()) {
            binding.etNamaPemilik.setInfoError(getString(R.string.error_field_required))
        } else if (username.isEmpty()) {
            binding.etUsername.setInfoError(getString(R.string.error_field_required))
        } else if (nama_umkm.isEmpty()) {
            binding.etNamaUmkm.setInfoError(getString(R.string.error_field_required))
        } else if (password.isEmpty()) {
            binding.etPassword.setInfoError(getString(R.string.error_field_required))
        } else if (confirm_password.isEmpty()) {
            binding.etCPassword.setInfoError(getString(R.string.error_field_required))
        } else if (password != confirm_password) {
            binding.etCPassword.setInfoError(getString(R.string.error_field_password_not_match))
        } else {
            user = User(name, username, password, nama_umkm)
            user?.let {
                viewModel.addUser(it)
            }

        }
    }

    private fun observeData() {
        viewModel.responseAdd.observe(this) {
            when (it.status) {
                StatusRequest.SUCCESS -> {
                    user?.let { account ->
                        saveUser(account)
                    }
                }
                else -> {
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUser(user: User) {
        viewModel.saveUsername(user.username)
        viewModel.savePassword(user.password)
        gotoMainPage()
    }

    private fun gotoMainPage() {
        val i = Intent(this@RegisterActivity, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}