package com.tapisdev.penjualankasir.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.penjualankasir.activity.HistoryTransaksiActivity
import com.tapisdev.penjualankasir.activity.SelectPelangganActivity
import com.tapisdev.penjualankasir.databinding.ItemsPelangganBinding
import com.tapisdev.penjualankasir.databinding.ItemsTransaksiBinding
import com.tapisdev.penjualankasir.model.Pelanggan
import com.tapisdev.penjualankasir.model.Transaksi
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterTransaksi(private val list:ArrayList<Transaksi>) : RecyclerView.Adapter<AdapterTransaksi.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsTransaksiBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list?.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsTransaksiBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){
                val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
                val df = nf as DecimalFormat

                var nama_pelanggan = ""
                if (list?.get(position).nama_pelanggan == null || list?.get(position).nama_pelanggan.equals("")){
                    nama_pelanggan = "Guest"
                }else{
                    nama_pelanggan = list?.get(position).nama_pelanggan!!
                }

                binding.tvNamaPelanggan.setText(nama_pelanggan)
                binding.tvTanggal.setText(""+list?.get(position).tgl_transaksi)
                binding.tvTotalBayar.setText("Total bayar Rp. "+df.format(list?.get(position).total_bayar)+" -  Untung "+df.format(list?.get(position).total_untung))

                binding.rlTransaksi.setOnClickListener {

                    val i  = Intent(binding.rlTransaksi.context,HistoryTransaksiActivity::class.java)
                    i.putExtra("transaksi",list?.get(position))
                    binding.rlTransaksi.context.startActivity(i)
                }

            }
        }

    }

}