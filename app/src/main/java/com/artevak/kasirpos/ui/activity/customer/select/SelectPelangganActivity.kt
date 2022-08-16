package com.artevak.kasirpos.ui.activity.customer.select

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.R
import com.artevak.kasirpos.ui.adapter.AdapterPelanggan
import com.artevak.kasirpos.databinding.ActivitySelectPelangganBinding
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.data.model.SharedVariable
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.artevak.kasirpos.ui.activity.customer.add.TambahPelangganViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectPelangganActivity : BaseActivity() {
    lateinit var binding: ActivitySelectPelangganBinding
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var adapter: AdapterPelanggan
    var listCustomer = ArrayList<ResponseData<Customer>>()

    var TAG_GET_PELANGGAN = "pelanggan"
    var TAG_GET_MORE_PELANGGAN = "morepelanggan"
    var CURRENT_PAGE = 1
    var NEXT_PAGE = CURRENT_PAGE + 1
    var KATA_KUNCI = ""

    val viewModel: SelectPelangganViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPelangganBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        shimmerFrameLayout = view.findViewById(R.id.sflMain)
        adapter = AdapterPelanggan(listCustomer)

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPelanggan.setHasFixedSize(true)
        binding.rvPelanggan.layoutManager = layoutManager
        binding.rvPelanggan.adapter = adapter

        initListener()

        observeData()

        getDataPelanggan()
    }

    private fun observeData() {
        viewModel.items.observe(this) {
            when (it.status) {
                StatusRequest.LOADING -> {
                    showLoadingShimmerPelanggan()
                }
                StatusRequest.SUCCESS -> {
                    hideLoadingShimmerPelanggan()
                    listCustomer.clear()
                    it.data?.let { it1 -> listCustomer.addAll(it1) }
                    adapter.notifyDataSetChanged()

                    if (listCustomer.size == 0) {
                        binding.tvInfoEmpty.visibility = View.VISIBLE
                    }
                }
                else -> {
                    hideLoadingShimmerPelanggan()
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initListener(){
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.rbPelangganGuest.setOnCheckedChangeListener { compoundButton, b ->
            if (b == true) {
                SharedVariable.pelangganType = "guest"
                onBackPressed()
            }
        }
    }
    fun setSelectedPelanggan(customer: Customer) {
        SharedVariable.pelangganType = "registered"
        SharedVariable.selectedCustomer = customer
        onBackPressed()
    }

    fun getDataPelanggan() {
        showLoadingShimmerPelanggan()

        viewModel.getCustomers()
        hideLoadingShimmerPelanggan()
    }

    fun showLoadingShimmerPelanggan() {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        binding.rvPelanggan.visibility = View.GONE
        binding.tvInfoEmpty.visibility = View.GONE
    }

    fun hideLoadingShimmerPelanggan() {
        if (shimmerFrameLayout.isVisible) {
            shimmerFrameLayout.stopShimmerAnimation()
            shimmerFrameLayout.clearAnimation()
            shimmerFrameLayout.visibility = View.GONE
        }

        binding.rvPelanggan.visibility = View.VISIBLE
    }
}