package com.tapisdev.penjualankasir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.penjualankasir.activity.HomeActivity
import com.tapisdev.penjualankasir.activity.RegisterActivity
import com.tapisdev.penjualankasir.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding  : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            val i = Intent(this,HomeActivity::class.java)
            startActivity(i)
        }
        binding.tvKeRegister.setOnClickListener {
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }
    }
}