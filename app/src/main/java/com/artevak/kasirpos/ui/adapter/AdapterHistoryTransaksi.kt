package com.artevak.kasirpos.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.artevak.kasirpos.BuildConfig
import com.artevak.kasirpos.databinding.ItemsKeranjangBinding
import com.artevak.kasirpos.model.HistoryTransaksi
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterHistoryTransaksi(private val list:ArrayList<HistoryTransaksi>) : RecyclerView.Adapter<AdapterHistoryTransaksi.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsKeranjangBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsKeranjangBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
                val df = nf as DecimalFormat

                val imageUrl = "img/barang/"+ list[position].picture
               // Log.d("imgbarang",""+imageUrl)
                Glide.with(binding.rlBarang.context)
                    .load(imageUrl)
                    .into(binding.ivBarang)

                binding.tvNamaBarang.setText(""+ list[position].nama_barang)
                binding.tvJumlahBeli.setText("Subtotal Rp. "+df.format(list[position].subtotal))

                binding.btnDeleteKeranjang.visibility = View.GONE

            }
        }

    }

}