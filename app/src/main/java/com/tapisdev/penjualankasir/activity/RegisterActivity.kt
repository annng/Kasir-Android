package com.tapisdev.penjualankasir.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}