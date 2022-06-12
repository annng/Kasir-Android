package com.tapisdev.penjualankasir.adapter


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.penjualankasir.databinding.ItemsBarangBinding
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.BuildConfig
import com.tapisdev.penjualankasir.activity.DetailBarangActivity
import com.tapisdev.penjualankasir.databinding.ItemsKeranjangBinding
import com.tapisdev.penjualankasir.fragment.TransaksiFragment
import com.tapisdev.penjualankasir.model.Keranjang
import com.tapisdev.penjualankasir.fragment.HomeFragment
import com.tapisdev.penjualankasir.model.HistoryTransaksi
import com.tapisdev.penjualankasir.model.SharedVariable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterHistoryTransaksi(private val list:ArrayList<HistoryTransaksi>) : RecyclerView.Adapter<AdapterHistoryTransaksi.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsKeranjangBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list?.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsKeranjangBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){
                val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
                val df = nf as DecimalFormat

                val imageUrl = BuildConfig.BASE_URL+"img/barang/"+list?.get(position).picture
               // Log.d("imgbarang",""+imageUrl)
                Glide.with(binding.rlBarang.context)
                    .load(imageUrl)
                    .into(binding.ivBarang)

                binding.tvNamaBarang.setText(""+list?.get(position).nama_barang)
                binding.tvJumlahBeli.setText("Subtotal Rp. "+df.format(list?.get(position).subtotal))

                binding.btnDeleteKeranjang.visibility = View.GONE

            }
        }

    }

}