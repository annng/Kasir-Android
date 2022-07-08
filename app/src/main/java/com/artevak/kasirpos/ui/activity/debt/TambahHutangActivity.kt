package com.artevak.kasirpos.ui.activity.debt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.artevak.kasirpos.ui.activity.customer.SelectPelangganActivity
import com.artevak.lokamotor.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahHutangBinding
import com.artevak.kasirpos.model.HutangInfo
import com.artevak.kasirpos.model.SharedVariable
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        mUserPref = UserPreference(this)

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
        showLoading(this)
        Log.d(TAG_HUTANG," "+hutangInfo.toString())

        ApiMain().services.saveHutang(mUserPref.getToken(),hutangInfo).enqueue(
            object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toasty.error(this@TambahHutangActivity, "gagal simpan transaksi, coba lagi nanti", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_HUTANG,t.message.toString())
                    dismissLoading()
                }
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    val responAPI = response.body()
                    val responseStatus = response.code()


                    dismissLoading()
                    if(response.code() == 200) {
                        Log.d(TAG_HUTANG,"body "+responAPI!!.toString())
                        Log.d(TAG_HUTANG,"http code asli "+responseStatus.toString())
                        Log.d(TAG_HUTANG,"http code dari API "+responAPI!!.http_status)

                        Toasty.success(this@TambahHutangActivity, "Tambah hutang berhasil !", Toast.LENGTH_SHORT, true).show()
                        onBackPressed()

                    }else if (response.code() == 202){
                        Toasty.error(this@TambahHutangActivity, "gagal simpan transaksi, coba lagi nanti", Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        )
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