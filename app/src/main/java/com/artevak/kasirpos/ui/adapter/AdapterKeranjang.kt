package com.artevak.kasirpos.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.artevak.kasirpos.BuildConfig
import com.artevak.kasirpos.databinding.ItemsKeranjangBinding
import com.artevak.kasirpos.ui.fragment.TransaksiFragment
import com.artevak.kasirpos.model.Keranjang


class AdapterKeranjang(private val list:ArrayList<Keranjang>,private val fragment: TransaksiFragment) : RecyclerView.Adapter<AdapterKeranjang.MyViewHolder>(){



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

                val imageUrl = BuildConfig.BASE_URL+"img/barang/"+list?.get(position).picture
               // Log.d("imgbarang",""+imageUrl)
                Glide.with(binding.rlBarang.context)
                    .load(imageUrl)
                    .into(binding.ivBarang)

                binding.tvNamaBarang.setText(""+list?.get(position).nama_barang)
                binding.tvJumlahBeli.setText(""+list?.get(position).jumlah_beli+" "+list?.get(position).satuan)

                binding.btnDeleteKeranjang.setOnClickListener {
                    (fragment as TransaksiFragment).removeFromCart(list?.get(position))
                }

            }
        }

    }

}