package com.artevak.kasirpos.ui.activity.transaction.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.ui.adapter.AdapterHistoryTransaksi
import com.artevak.kasirpos.databinding.ActivityHistoryTransaksiBinding
import com.artevak.kasirpos.data.model.Transaction
import com.artevak.kasirpos.data.model.Transaksi
import kotlinx.android.synthetic.main.fragment_stok.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class HistoryTransaksiActivity : BaseActivity() {
    lateinit var transaksi : Transaksi
    lateinit var binding : ActivityHistoryTransaksiBinding
    lateinit var i : Intent
    lateinit var adapter: AdapterHistoryTransaksi

    var listTransaction = ArrayList<Transaction>()
    val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
    val df = nf as DecimalFormat

    var TAG_GET_HISTORY = "history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTransaksiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        i = intent
        transaksi = i.getParcelableExtra("transaksi")!!

        adapter = AdapterHistoryTransaksi(listTransaction)
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

        //TODO get data transaksi
        hideLoadingPG()
    }

    fun hideLoadingPG(){
        progressBar.visibility = View.GONE
        binding.rvTransaksi.visibility = View.VISIBLE
    }
}