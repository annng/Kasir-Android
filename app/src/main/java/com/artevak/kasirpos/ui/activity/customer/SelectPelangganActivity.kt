package com.artevak.kasirpos.ui.activity.customer

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.R
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import com.artevak.kasirpos.databinding.ActivitySelectPelangganBinding
import com.artevak.kasirpos.data.model.Pelanggan
import com.artevak.kasirpos.data.model.SharedVariable

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

        //TODO get data pelanggan
        listPelanggan.add(
            Pelanggan(
                name = "Anang",
                phone = "+6285747325450",
                alamat = "Soka, Lerep"
            )
        )
        listPelanggan.add(
            Pelanggan(
                name = "Ardhea",
                phone = "+6285747123456",
                alamat = "Banyumanik"
            )
        )
        hideLoadingShimmerPelanggan()
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