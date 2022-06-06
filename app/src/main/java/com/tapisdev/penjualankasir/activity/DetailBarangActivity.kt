package com.tapisdev.penjualankasir.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tapisdev.lokamotor.base.BaseActivity
import com.tapisdev.penjualankasir.BuildConfig
import com.tapisdev.penjualankasir.R
import com.tapisdev.penjualankasir.databinding.ActivityDetailBarangBinding
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.model.UserPreference
import com.tapisdev.penjualankasir.response.BarangResponse
import com.tapisdev.penjualankasir.response.CommonResponse
import com.tapisdev.penjualankasir.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailBarangActivity : BaseActivity() {
    lateinit var binding : ActivityDetailBarangBinding
    lateinit var i : Intent
    lateinit var barang : Barang

    var TAG_DELETE = "deletebarang"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        i = intent
        barang = i.getParcelableExtra("barang")!!

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnEdit.setOnClickListener {
            val i = Intent(this,UbahBarangActivity::class.java)
            i.putExtra("barang",barang)
            startActivity(i)
        }
        binding.btnDelete.setOnClickListener {
            deleteBarang(barang.id!!)
        }

        updateUI()
    }

    fun updateUI(){
        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        val imageURl = BuildConfig.BASE_URL+"img/barang/"+barang.picture
        Glide.with(this)
            .load(imageURl)
            .into(binding.ivFoto)

        binding.tvNamaBarang.setText(barang.name)
        binding.tvStok.setText(""+barang.stok)
        binding.tvDeskripsi.setText(barang.deskripsi)
        binding.tvSatuan.setText(barang.satuan)
        binding.tvHargaBeli.setText(""+df.format(barang.harga_beli))
        binding.tvHargaJual.setText(""+df.format(barang.harga_jual))
    }

    fun deleteBarang(id_barang : String){
        showLoading(this)

        ApiMain().services.deleteBarang(mUserPref.getToken(),id_barang).enqueue(object :
            retrofit2.Callback<CommonResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                dismissLoading()
                //Tulis code jika response sukses
                Log.d(TAG_DELETE,response.toString())
                Log.d(TAG_DELETE,"http status : "+response.code())

                if(response.code() == 200) {
                    showSuccessMessage("Hapus barang berhasil !")
                    onBackPressed()
                }else {
                    Toasty.error(this@DetailBarangActivity, "hapus barang gagal", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_DELETE,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable){
                //Tulis code jika response fail
                dismissLoading()
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_DELETE,"rusak nya gpapa kok  ")
                }else{
                    Toasty.error(this@DetailBarangActivity, "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_DELETE,"rusak : "+t.message.toString())
                }
            }
        })
    }
}