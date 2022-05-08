package com.tapisdev.penjualankasir.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.databinding.ActivityTambahBarangBinding

class TambahBarangActivity : AppCompatActivity() {
    lateinit var binding : ActivityTambahBarangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}