package com.artevak.kasirpos.ui.activity.transaction

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.adapter.AdapterHistoryTransaksi
import com.artevak.kasirpos.databinding.ActivityHistoryTransaksiBinding
import com.artevak.kasirpos.model.HistoryTransaksi
import com.artevak.kasirpos.model.Transaksi
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.HistoryTransaksiResponse
import com.artevak.kasirpos.util.ApiMain
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_stok.*
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class HistoryTransaksiActivity : BaseActivity() {
    lateinit var transaksi : Transaksi
    lateinit var binding : ActivityHistoryTransaksiBinding
    lateinit var i : Intent
    lateinit var adapter: AdapterHistoryTransaksi

    var listHistoryTransaksi = ArrayList<HistoryTransaksi>()
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat

    var TAG_GET_HISTORY = "history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTransaksiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)
        i = intent
        transaksi = i.getParcelableExtra<Transaksi>("transaksi")!!

        adapter = AdapterHistoryTransaksi(listHistoryTransaksi)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTransaksi.setHasFixedSize(true)
        binding.rvTransaksi.layoutManager = layoutManager
        binding.rvTransaksi.adapter = adapter


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        updateUI()
        getDataTransaksi()
    }

    fun updateUI(){
        if(transaksi.nama_pelanggan == null || transaksi.nama_pelanggan.equals("")){
            binding.tvNamaPelanggan.setText("Guest")
        }else{
            binding.tvNamaPelanggan.setText(transaksi.nama_pelanggan)
        }
        binding.tvDeskripsi.setText("  Total Bayar Rp. "+df.format(transaksi.total_bayar)+ " \n "+" Total untung Rp. "+df.format(transaksi.total_untung))
    }

    fun getDataTransaksi(){
        binding.progressBar.visibility = View.VISIBLE

        ApiMain().services.getHistoryTransaksi(mUserPref.getToken(),transaksi.id).enqueue(object :
            retrofit2.Callback<HistoryTransaksiResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<HistoryTransaksiResponse>, response: Response<HistoryTransaksiResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_HISTORY,response.toString())
                Log.d(TAG_GET_HISTORY,"http status : "+response.code())

                if(response.code() == 200) {
                    listHistoryTransaksi.clear()
                    response.body()?.data_history?.let {
                        Log.d(TAG_GET_HISTORY,"dari API : "+it)
                        Log.d(TAG_GET_HISTORY,"jumlah dari API : "+it.size)
                        listHistoryTransaksi.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingPG()
                        Log.d(TAG_GET_HISTORY,"isi adapter  : "+adapter.itemCount)
                    }


                }else {
                    Toasty.error(this@HistoryTransaksiActivity, "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_HISTORY,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<HistoryTransaksiResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_HISTORY,"rusak nya gpapa kok  ")
                    hideLoadingPG()
                }else{
                    Toasty.error(this@HistoryTransaksiActivity, "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_HISTORY,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun hideLoadingPG(){
        progressBar.visibility = View.GONE
        binding.rvTransaksi.visibility = View.VISIBLE
    }
}