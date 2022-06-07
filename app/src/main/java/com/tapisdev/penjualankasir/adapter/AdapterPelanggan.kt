package com.tapisdev.penjualankasir.adapter


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.penjualankasir.databinding.ItemsBarangBinding
import com.tapisdev.penjualankasir.model.Barang
import com.tapisdev.penjualankasir.BuildConfig
import com.tapisdev.penjualankasir.activity.DetailBarangActivity
import com.tapisdev.penjualankasir.databinding.ItemsPelangganBinding
import com.tapisdev.penjualankasir.model.Pelanggan

class AdapterPelanggan(private val list:ArrayList<Pelanggan>) : RecyclerView.Adapter<AdapterPelanggan.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsPelangganBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list?.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsPelangganBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){


                binding.tvNamaPelanggan.setText(""+list?.get(position).name)
                binding.tvPhone.setText(""+list?.get(position).phone)
                binding.tvAlamat.setText(list?.get(position).alamat)

                binding.rlPelanggan.setOnClickListener {
                   /* val i  = Intent(binding.rlPelanggan.context,DetailBarangActivity::class.java)
                    i.putExtra("pelanggan",list?.get(position))
                    binding.rlPelanggan.context.startActivity(i)*/
                }

            }
        }

    }

}