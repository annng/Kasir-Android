package com.artevak.kasirpos.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.artevak.kasirpos.databinding.ItemsBarangBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.ui.activity.item.DetailBarangActivity

class AdapterBarang(private val list:ArrayList<Barang>) : RecyclerView.Adapter<AdapterBarang.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsBarangBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsBarangBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){

                val imageUrl = ""+ list.get(position).picture
               // Log.d("imgbarang",""+imageUrl)
                Glide.with(binding.rlBarang.context)
                    .load(imageUrl)
                    .into(binding.ivBarang)

                binding.tvNamaBarang.setText(""+ list.get(position).name)
                binding.tvStok.setText("Sisa Stok "+ list.get(position).stok +" "+ list.get(position).satuan)
                binding.tvDeskripsi.setText(list.get(position).deskripsi)

                binding.rlBarang.setOnClickListener {
                    val i  = Intent(binding.rlBarang.context, DetailBarangActivity::class.java)
                    i.putExtra("barang",list?.get(position))
                    binding.rlBarang.context.startActivity(i)
                }

            }
        }

    }

}