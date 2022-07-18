package com.artevak.kasirpos.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.ui.activity.customer.SelectPelangganActivity
import com.artevak.kasirpos.databinding.ItemsPelangganBinding
import com.artevak.kasirpos.data.model.Pelanggan

class AdapterPelanggan(private val list:ArrayList<Pelanggan>) : RecyclerView.Adapter<AdapterPelanggan.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = ItemsPelangganBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    inner class MyViewHolder(val binding: ItemsPelangganBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(list.get(position)){

                binding.tvNamaPelanggan.text = ""+ list[position].name
                binding.tvPhone.text = ""+ list[position].phone
                binding.tvAlamat.text = list[position].alamat

                binding.rlPelanggan.setOnClickListener {

                    if (binding.rlPelanggan.context is SelectPelangganActivity) {
                        (binding.rlPelanggan.context as SelectPelangganActivity).setSelectedPelanggan(
                            list[position]
                        )
                    }

                   /* val i  = Intent(binding.rlPelanggan.context,DetailBarangActivity::class.java)
                    i.putExtra("pelanggan",list?.get(position))
                    binding.rlPelanggan.context.startActivity(i)*/
                }

            }
        }

    }

}