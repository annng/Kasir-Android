package com.tapisdev.penjualankasir.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.lokamotor.base.BaseActivity
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.databinding.ActivityTambahPelangganBinding

class TambahPelangganActivity : BaseActivity() {
    lateinit var binding : ActivityTambahPelangganBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPelangganBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}