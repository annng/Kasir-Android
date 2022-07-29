package com.artevak.kasirpos.ui.activity.profile.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isEmpty
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.util.ext.dashIfEmpty
import com.artevak.kasirpos.data.model.User
import com.artevak.kasirpos.databinding.ActivityProfileEditBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileEditActivity : BaseActivity(), View.OnClickListener {

    val viewModel: ProfileEditViewModel by viewModel()

    companion object {
        fun generateIntent(context: Context) {
            val i = Intent(context, ProfileEditActivity::class.java)
            context.startActivity(i)
        }
    }

    val binding: ActivityProfileEditBinding by lazy {
        ActivityProfileEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initListener()
        observeData()
        viewModel.getMyAccount()
    }

    override fun initListener() {
        binding.ivBack.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }


    private fun setUI(user: User) {
        with(binding) {
            etUsername.text = user.username
            etPassword.text = user.password
            etShopName.text = user.shopeName.dashIfEmpty()
            etShopAddress.text = user.address.dashIfEmpty()
            etOwnerName.text = user.name.dashIfEmpty()
            etShopPhone.text = user.phone.dashIfEmpty()
        }
    }

    private fun observeData() {
        viewModel.user.observe(this) {
            setUI(it)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivBack -> onBackPressed()
            binding.btnSave -> {

                if (binding.etOwnerName.isEmpty()){
                    binding.etOwnerName.setInfoError(getString(R.string.error_field_required))
                    return
                }

                if (binding.etShopName.isEmpty()){
                    binding.etShopName.setInfoError(getString(R.string.error_field_required))
                    return
                }

                var password = viewModel.getPassword()
                if (viewModel.isNeedUpdatePassword(binding.etPassword.text, binding.etCPassword.text)){
                    val isPasswordConfirmed = binding.etPassword.text == binding.etCPassword.text
                    if (!isPasswordConfirmed) {
                        binding.etCPassword.setInfoError(getString(R.string.error_field_password_not_match))
                        return
                    }

                    password = binding.etPassword.text
                }

                viewModel.updateAccount(User(
                    name = binding.etOwnerName.text,
                    username = binding.etUsername.text,
                    password = password,
                    shopeName = binding.etShopName.text,
                    address = binding.etShopAddress.text,
                    phone = binding.etShopPhone.text
                ))

                Toast.makeText(this, getString(R.string.alert_toast_success_update_account), Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }
}