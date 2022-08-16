package com.artevak.kasirpos.ui.activity.customer.add

import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.databinding.ActivityTambahPelangganBinding
import com.artevak.kasirpos.data.model.PelangganInfo
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.item.add.TambahBarangViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TambahPelangganActivity : BaseActivity() {
    lateinit var binding : ActivityTambahPelangganBinding

    val viewModel: TambahPelangganViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPelangganBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }

        observeData()
    }

    private fun observeData(){
        viewModel.addCustomer.observe(this){
            when(it.status){
                StatusRequest.LOADING -> showLoading(this)
                StatusRequest.SUCCESS -> {
                    dismissLoading()
                    onBackPressed()
                }
                else -> {
                    it.message?.let { it1 -> showErrorMessage(it1) }
                }
            }
        }
    }

    fun checkValidation(){
        val name = binding.etNamaPelanggan.text
        val phone = binding.etPhone.text
        val alamat = binding.etAlamat.text

        if (name.equals("") || name.length == 0){
            showErrorMessage("Nama belum diisi")
        }else if (phone.equals("") || phone.length == 0){
            showErrorMessage("Telepom belum diisi")
        }else if (alamat.equals("") || alamat.length == 0){
            showErrorMessage("Alamat belum diisi")
        }else{
            val pelangganInfo = Customer(name,phone,alamat)
            savePelanggan(pelangganInfo)
        }
    }

    fun savePelanggan(pelangganInfo: Customer){
        viewModel.addCustomer(pelangganInfo)
    }
}