package com.artevak.kasirpos.ui.activity.profile.view

import android.content.Intent
import android.os.Bundle
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.const.Cons
import com.artevak.kasirpos.common.util.ext.dashIfEmpty
import com.artevak.kasirpos.data.model.Menu
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.ui.activity.auth.login.LoginActivity
import com.artevak.kasirpos.databinding.ActivityProfileBinding
import com.artevak.kasirpos.ui.activity.profile.edit.ProfileEditActivity
import com.artevak.kasirpos.ui.activity.web.WebMarkDownActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {
    lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModel()
    private val menus = ArrayList<Menu>()
    private val adapter: ProfileMenuAdapter by lazy {
        ProfileMenuAdapter(menus) {
            when(menus[it].title){
                getString(R.string.title_menu_privacy_policy) -> {
                    val i = WebMarkDownActivity.generateIntent(this, Cons.MARKDOWN.PRIVACY_POLICY, menus[it].title)
                    startActivity(i)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.lineLogout.setOnClickListener {
            sendLogoutRequest()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvEditProfile.setOnClickListener {
            ProfileEditActivity.generateIntent(this)
        }

        observeData()
        viewModel.getUser()
        initAdapter()
        setData()
    }

    private fun observeData() {
        viewModel.user.observe(this) {
            updateUI(it)
        }
    }

    private fun setData() {
        menus.clear()
        menus.addAll(viewModel.getMenu())

        adapter.notifyDataSetChanged()
    }

    private fun updateUI(user: User) {
        val firstChara = user.name.take(1)
        binding.tvInisial.text = firstChara
        binding.tvName.text = user.name.dashIfEmpty()
        binding.tvEmail.text = user.shopeName.dashIfEmpty()
        binding.tvPhone.text = user.phone.dashIfEmpty()
    }

    override fun initAdapter() {
        super.initAdapter()
        binding.rvMenu.adapter = adapter
    }

    private fun sendLogoutRequest() {
        viewModel.logout()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}