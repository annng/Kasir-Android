package com.artevak.kasirpos.ui.activity.customer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.artevak.kasirpos.ui.activity.splash.SplashActivity
import com.artevak.lokamotor.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahPelangganBinding
import com.artevak.kasirpos.model.PelangganInfo
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        showLoading(this)

        ApiMain().services.savePelanggan(mUserPref.getToken(),pelangganInfo).enqueue(
            object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    showErrorMessage("gagal melakukan login, coba lagi nanti")
                    Log.d(TAG_SAVE,t.message.toString())
                    dismissLoading()
                }
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    val responAPI = response.body()
                    val responseStatus = response.code()


                    dismissLoading()
                    if(response.code() == 200) {
                        Log.d(TAG_SAVE,"body "+responAPI!!.toString())
                        Log.d(TAG_SAVE,"http code asli "+responseStatus.toString())
                        Log.d(TAG_SAVE,"http code dari API "+responAPI!!.http_status)

                        showSuccessMessage(responAPI.message)
                        onBackPressed()

                    }else if (response.code() == 202){
                        showErrorMessage(responAPI!!.message)
                    }else if (response.code() == 401){
                        showErrorMessage("Terjadi eror pada token, login kembali...")
                        logout()
                        val i = Intent(this@TambahPelangganActivity, SplashActivity::class.java)
                        startActivity(i)
                    }
                }
            }
        )
    }
}