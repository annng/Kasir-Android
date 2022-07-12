package com.artevak.kasirpos.ui.activity.customer

import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahPelangganBinding
import com.artevak.kasirpos.model.PelangganInfo
import com.artevak.kasirpos.model.UserPreference

class TambahPelangganActivity : BaseActivity() {
    lateinit var binding : ActivityTambahPelangganBinding

    var TAG_SAVE = "savepelanggan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPelangganBinding.inflate(layoutInflater)
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
        val name = binding.etNamaPelanggan.text.toString()
        val phone = binding.etPhone.text.toString()
        val alamat = binding.etAlamat.text.toString()

        if (name.equals("") || name.length == 0){
            showErrorMessage("Nama belum diisi")
        }else if (phone.equals("") || phone.length == 0){
            showErrorMessage("Telepom belum diisi")
        }else if (alamat.equals("") || alamat.length == 0){
            showErrorMessage("Alamat belum diisi")
        }else{
            val pelangganInfo = PelangganInfo(name,phone,alamat)
            savePelanggan(pelangganInfo)
        }
    }

    fun savePelanggan(pelangganInfo: PelangganInfo){
        Toast.makeText(this, "Save data pelanggan", Toast.LENGTH_SHORT).show()
        onBackPressed()

        //TODO save pelanggan info
    }
}