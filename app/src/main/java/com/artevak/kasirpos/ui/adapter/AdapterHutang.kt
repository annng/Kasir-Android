package com.artevak.kasirpos.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.ui.activity.debt.DetailHutangActivity
import com.artevak.kasirpos.databinding.ItemsHutangBinding
import com.artevak.kasirpos.data.model.Hutang
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterHutang(private val list:ArrayList<Hutang>) : RecyclerView.Adapter<AdapterHutang.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsHutangBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsHutangBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){
                val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
                val df = nf as DecimalFormat

                var nama_pelanggan = ""
                nama_pelanggan = if (list[position].nama_pelanggan == null || list[position].nama_pelanggan.equals("")){
                    "Guest"
                } else{
                    list[position].nama_pelanggan!!
                }

                if (list[position].hutang_type.equals("saya")){
                    binding.tvNamaPelanggan.text = list[position].deskripsi
                }else{
                    binding.tvNamaPelanggan.text = nama_pelanggan
                }
                binding.tvTanggal.text = ""+ list[position].tgl_hutang
                binding.tvTotalBayar.text = "Hutang Rp. "+df.format(list[position].hutang)
                binding.tvStatusHutang.text = list[position].status

                binding.rlTransaksi.setOnClickListener {
                    val i = Intent(binding.rlTransaksi.context, DetailHutangActivity::class.java)
                    i.putExtra("hutang", list.get(position))
                    binding.rlTransaksi.context.startActivity(i)
                }


            }
        }

    }

}