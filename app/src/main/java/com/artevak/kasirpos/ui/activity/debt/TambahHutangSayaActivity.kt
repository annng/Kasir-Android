package com.artevak.kasirpos.ui.activity.debt

import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahHutangSayaBinding
import com.artevak.kasirpos.data.model.HutangInfo
import com.artevak.kasirpos.data.model.SharedVariable

class TambahHutangSayaActivity : BaseActivity() {

    lateinit var binding  : ActivityTambahHutangSayaBinding
    lateinit var hutangInfo: HutangInfo
    var id_pelanggan = "0"
    var TAG_HUTANG = "hutang"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahHutangSayaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        val nominal = binding.etHutang.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()

        if (nominal.equals("") || nominal.length == 0){
            showErrorMessage("Hutang belum diisi")
        }else if (deskripsi.equals("") || deskripsi.length == 0){
            showErrorMessage("Deskripsi belum diisi")
        }
        else{

            val nominal_hutang = nominal.toInt()
            val hutang_type = "saya"

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
        //TODO save hutang saya
        Toast.makeText(this, "Save hutang saya", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }
}