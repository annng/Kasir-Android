package com.artevak.kasirpos.ui.activity.debt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityDetailHutangBinding
import com.artevak.kasirpos.data.model.Hutang
import com.artevak.kasirpos.common.util.ext.parseString
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailHutangActivity : BaseActivity() {
    lateinit var binding : ActivityDetailHutangBinding
    lateinit var hutang : Hutang
    lateinit var i : Intent
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat
    
    var TAG_UPDATE_HUTANG = "updatehutang"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHutangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        i = intent
        hutang = i.getParcelableExtra<Hutang>("hutang")!!

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.rbSudahLunas.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                hutang.status = "lunas"
            }
        }
        binding.rbBelumLunas.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                hutang.status = "belum lunas"
            }
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }

        updateUI()
    }

    fun updateUI(){
        binding.etHutang.isEnabled = false
        binding.etHutang.setText(hutang.hutang.parseString())

        var nama_pelanggan = ""
        if (hutang.nama_pelanggan == null || hutang.nama_pelanggan.equals("")){
            nama_pelanggan = "Guest"
        }
        else{
            nama_pelanggan = hutang.nama_pelanggan!!
        }
        binding.tvNamaPelanggan.setText(nama_pelanggan)

        if (hutang.hutang_type.equals("saya")){
            binding.etDeskripsi.setText(hutang.deskripsi)
            binding.etDeskripsi.visibility = View.VISIBLE
        }

        if (hutang.status.equals("lunas")){
            binding.rbSudahLunas.isChecked = true
        }else{
            binding.rbBelumLunas.isChecked = true
        }
    }

    fun checkValidation(){
        val deskripsi =  binding.etDeskripsi.text.toString()

        if (hutang.hutang_type.equals("saya") && deskripsi.equals("")){
            showErrorMessage("Anda belum mengisi deskripsi")
        }else{
            hutang.deskripsi = deskripsi
            updateHutang()
        }
    }

    fun updateHutang(){
        Toast.makeText(this, "Updating Hutang", Toast.LENGTH_SHORT).show()

        //TODO update hutang
    }
}