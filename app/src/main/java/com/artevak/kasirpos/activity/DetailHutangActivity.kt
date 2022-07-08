package com.artevak.kasirpos.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.artevak.lokamotor.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityDetailHutangBinding
import com.artevak.kasirpos.model.Hutang
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        mUserPref = UserPreference(this)

        i = intent
        hutang = i.getParcelableExtra<Hutang>("hutang")!!

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.rbSudahLunas.setOnCheckedChangeListener { compoundButton, b ->
            if (b == true){
                hutang.status = "lunas"
            }
        }
        binding.rbBelumLunas.setOnCheckedChangeListener { compoundButton, b ->
            if (b == true){
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
        binding.etHutang.setText("Rp. "+df.format(hutang.hutang))

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
        showLoading(this)

        val builder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("id_hutang",hutang.id!!)
        builder.addFormDataPart("deskripsi",hutang.deskripsi!!)
        builder.addFormDataPart("status",hutang.status!!)
        val requestBody: RequestBody = builder.build()

        ApiMain().services.editHutang(mUserPref.getToken(),requestBody).enqueue(
            object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    showErrorMessage("gagal melakukan simpan data, coba lagi nanti")
                    Log.d(TAG_UPDATE_HUTANG,t.message.toString())
                    dismissLoading()
                }
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    val apiResponse = response.body()
                    val responseInfo = response.code()
                    Log.d(TAG_UPDATE_HUTANG,"body "+apiResponse!!.toString())
                    Log.d(TAG_UPDATE_HUTANG,"code "+responseInfo.toString())

                    dismissLoading()
                    if(response.code() == 200) {
                        showSuccessMessage(apiResponse.message)
                        onBackPressed()

                    }else if (response.code() == 202){
                        showErrorMessage(apiResponse.message)
                    }else if (response.code() == 401){
                        showErrorMessage("terjadi error pada token, login kembali..")
                        logout()
                        val i = Intent(this@DetailHutangActivity, SplashActivity::class.java)
                        startActivity(i)
                    }
                }
            }
        )
    }
}