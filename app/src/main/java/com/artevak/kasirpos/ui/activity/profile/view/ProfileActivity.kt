package com.artevak.kasirpos.ui.activity.profile.view

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.util.ext.dashIfEmpty
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.ui.activity.auth.login.LoginActivity
import com.artevak.kasirpos.databinding.ActivityProfileBinding
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {
    lateinit var binding : ActivityProfileBinding
    var TAG_LOGOUT = "logout"
    private val viewModel : ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.lineLogout.setOnClickListener {
            sendLogoutRequest()
            logout()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        binding.tvEditProfile.setOnClickListener {
            ProfileEditActivity.generateIntent(this)
        }

        observeData()
        viewModel.getUser()
    }

    private fun observeData(){
        viewModel.user.observe(this) {
            updateUI(it)
        }
    }

    private fun updateUI(user: User){
        val firstChara = user.name.take(1)
        binding.tvInisial.text = firstChara
        binding.tvName.text = user.name.dashIfEmpty()
        binding.tvEmail.text = user.shopeName.dashIfEmpty()
    }

    fun sendLogoutRequest(){
        //TODO logout
    }
}