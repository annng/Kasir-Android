package com.artevak.kasirpos.ui.activity.debt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahHutangSayaBinding
import com.artevak.kasirpos.model.HutangInfo
import com.artevak.kasirpos.model.SharedVariable
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        mUserPref = UserPreference(this)

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