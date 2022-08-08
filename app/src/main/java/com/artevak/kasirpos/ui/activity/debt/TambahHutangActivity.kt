package com.artevak.kasirpos.ui.activity.debt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.ui.activity.customer.select.SelectPelangganActivity
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahHutangBinding
import com.artevak.kasirpos.data.model.HutangInfo
import com.artevak.kasirpos.data.model.SharedVariable

class TambahHutangActivity : BaseActivity() {
    lateinit var binding : ActivityTambahHutangBinding
    lateinit var hutangInfo: HutangInfo
    var id_pelanggan = "0"

    var TAG_HUTANG = "hutang"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahHutangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.cardPelanggan.setOnClickListener {
            val i = Intent(this, SelectPelangganActivity::class.java)
            startActivity(i)
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }
        binding.btnKeTambahHutangSaya.setOnClickListener {
            val i = Intent(this, TambahHutangSayaActivity::class.java)
            startActivity(i)
        }

    }

    fun checkValidation(){
        val nominal = binding.etHutang.text.toString()

        if (nominal.equals("") || nominal.length == 0){
            showErrorMessage("Hutang belum diisi")
        }else if (SharedVariable.pelangganType.equals("registered") && SharedVariable.selectedPelanggan == null){
            showErrorMessage("pelanggan belum dipilih")
        }else{
            if (SharedVariable.pelangganType.equals("registered")){
                id_pelanggan = SharedVariable.selectedPelanggan?.id!!
            }
            val nominal_hutang = nominal.toInt()
            val hutang_type = "pelanggan"
            val deskripsi = ""
            hutangInfo = HutangInfo(
                id_pelanggan,
                SharedVariable.pelangganType,
                nominal_hutang,
                hutang_type,
                deskripsi
            )
            saveHutang()
        }
    }

    fun saveHutang(){
        //TODO save hutang
        Toast.makeText(this, "Save hutang", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    fun setPelangganInfo(){
        if (SharedVariable.pelangganType.equals("guest")){
            binding.tvNamaPelanggan.setText("Pelanggan Guest")
        }else{
            id_pelanggan = SharedVariable.selectedPelanggan?.id!!
            binding.tvNamaPelanggan.setText(SharedVariable.selectedPelanggan?.name)
        }
    }

    override fun onResume() {
        super.onResume()
        setPelangganInfo()
    }
}