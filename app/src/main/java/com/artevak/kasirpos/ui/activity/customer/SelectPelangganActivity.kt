package com.artevak.kasirpos.ui.activity.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.R
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import com.artevak.kasirpos.databinding.ActivitySelectPelangganBinding
import com.artevak.kasirpos.model.Pelanggan
import com.artevak.kasirpos.model.SharedVariable
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.PelangganResponse
import com.artevak.kasirpos.util.ApiMain
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Response

class SelectPelangganActivity : BaseActivity() {
    lateinit var binding : ActivitySelectPelangganBinding
    lateinit var shimmerFrameLayout : ShimmerFrameLayout
    lateinit var adapter : AdapterPelanggan
    var listPelanggan = ArrayList<Pelanggan>()

    var TAG_GET_PELANGGAN = "pelanggan"
    var TAG_GET_MORE_PELANGGAN = "morepelanggan"
    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var KATA_KUNCI = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPelangganBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        shimmerFrameLayout = view.findViewById(R.id.sflMain)
        adapter = AdapterPelanggan(listPelanggan)

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPelanggan.setHasFixedSize(true)
        binding.rvPelanggan.layoutManager = layoutManager
        binding.rvPelanggan.adapter = adapter

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.rbPelangganGuest.setOnCheckedChangeListener { compoundButton, b ->
            if (b == true){
                SharedVariable.pelangganType = "guest"
                onBackPressed()
            }
        }

        getDataPelanggan()
    }

    fun setSelectedPelanggan(pelanggan: Pelanggan){
        SharedVariable.pelangganType = "registered"
        SharedVariable.selectedPelanggan = pelanggan
        onBackPressed()
    }

    fun getDataPelanggan(){
        showLoadingShimmerPelanggan()

        ApiMain().services.getPelanggan(mUserPref.getToken(),CURRENT_PAGE,KATA_KUNCI).enqueue(object :
            retrofit2.Callback<PelangganResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PelangganResponse>, response: Response<PelangganResponse>) {
                //Tulis code jika response sukses
                Log.d(TAG_GET_PELANGGAN,response.toString())
                Log.d(TAG_GET_PELANGGAN,"http status : "+response.code())

                if(response.code() == 200) {
                    listPelanggan.clear()
                    response.body()?.data_pelanggan?.let {
                        Log.d(TAG_GET_PELANGGAN,"dari API : "+it)
                        Log.d(TAG_GET_PELANGGAN,"jumlah dari API : "+it.size)
                        listPelanggan.addAll(it)
                        adapter.notifyDataSetChanged()

                        hideLoadingShimmerPelanggan()
                        Log.d(TAG_GET_PELANGGAN,"isi adapter  : "+adapter.itemCount)
                    }

                    if (listPelanggan.size == 0){
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }

                }else {
                    Toasty.error(this@SelectPelangganActivity, "gagal mengambil data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_PELANGGAN,"err :"+response.message())
                }
            }
            override fun onFailure(call: Call<PelangganResponse>, t: Throwable){
                //Tulis code jika response fail
                val errMsg = t.message.toString()
                if (errMsg.takeLast(6).equals("$.null")){
                    Log.d(TAG_GET_PELANGGAN,"rusak nya gpapa kok  ")
                    hideLoadingShimmerPelanggan()
                }else{
                    Toasty.error(this@SelectPelangganActivity, "response failure for more data", Toast.LENGTH_SHORT, true).show()
                    Log.d(TAG_GET_PELANGGAN,"rusak : "+t.message.toString())
                }
            }
        })
    }

    fun showLoadingShimmerPelanggan(){
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvPelanggan.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmerPelanggan(){
        if (shimmerFrameLayout.isVisible){
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvPelanggan.visibility = View.VISIBLE
    }
}