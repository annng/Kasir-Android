package com.artevak.kasirpos.ui.activity.profile.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityProfileEditBinding

class ProfileEditActivity : BaseActivity() {
    val binding : ActivityProfileEditBinding by lazy{
        ActivityProfileEditBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}