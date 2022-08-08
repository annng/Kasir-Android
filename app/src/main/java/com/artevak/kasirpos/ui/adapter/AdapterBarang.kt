package com.artevak.kasirpos.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.artevak.kasirpos.databinding.ItemsBarangBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.ui.activity.item.detail.DetailBarangActivity

class AdapterBarang(private val list: ArrayList<ResponseData<Barang>>) :
    RecyclerView.Adapter<AdapterBarang.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsBarangBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(list[position].data) {

                val imageUrl = picture
                Glide.with(binding.rlBarang.context)
                    .load(imageUrl)
                    .into(binding.ivBarang)

                binding.tvNamaBarang.text = "" + name
                binding.tvStok.text = "Remaining Item : " + stok + " " + satuan
                binding.tvDeskripsi.text = deskripsi

                binding.rlBarang.setOnClickListener {
                    val i = DetailBarangActivity.generateIntent(
                        holder.binding.root.context,
                        this,
                        list[position].keys
                    )
                    binding.rlBarang.context.startActivity(i)
                }

            }
        }

    }

}