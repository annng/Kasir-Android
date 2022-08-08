package com.artevak.kasirpos.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.ui.activity.transaction.history.HistoryTransaksiActivity
import com.artevak.kasirpos.databinding.ItemsTransaksiBinding
import com.artevak.kasirpos.data.model.Transaksi
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterTransaksi(private val list:ArrayList<Transaksi>) : RecyclerView.Adapter<AdapterTransaksi.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsTransaksiBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsTransaksiBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){
                val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
                val df = nf as DecimalFormat

                var nama_pelanggan = ""
                nama_pelanggan = if (list[position].nama_pelanggan == null || list.get(position).nama_pelanggan.equals("")){
                    "Guest"
                }else{
                    list[position].nama_pelanggan!!
                }

                binding.tvNamaPelanggan.text = nama_pelanggan
                binding.tvTanggal.text = ""+ list[position].tgl_transaksi
                binding.tvTotalBayar.text = "Total bayar Rp. "+df.format(list[position].total_bayar)+" -  Untung "+df.format(
                    list.get(position).total_untung)

                binding.rlTransaksi.setOnClickListener {

                    val i  = Intent(binding.rlTransaksi.context, HistoryTransaksiActivity::class.java)
                    i.putExtra("transaksi", list[position])
                    binding.rlTransaksi.context.startActivity(i)
                }

            }
        }

    }

}