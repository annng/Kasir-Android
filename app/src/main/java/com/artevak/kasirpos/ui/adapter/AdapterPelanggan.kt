package com.artevak.kasirpos.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artevak.kasirpos.ui.activity.customer.select.SelectPelangganActivity
import com.artevak.kasirpos.databinding.ItemsPelangganBinding
import com.artevak.kasirpos.data.model.Customer
import com.artevak.kasirpos.response.firebase.ResponseData

class AdapterPelanggan(private val list:ArrayList<ResponseData<Customer>>) : RecyclerView.Adapter<AdapterPelanggan.MyViewHolder>(){



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
            with(list.get(position).data){

                binding.tvNamaPelanggan.text = ""+ name
                binding.tvPhone.text = ""+ phone
                binding.tvAlamat.text = alamat

                binding.rlPelanggan.setOnClickListener {

                    if (binding.rlPelanggan.context is SelectPelangganActivity) {
                        (binding.rlPelanggan.context as SelectPelangganActivity).setSelectedPelanggan(
                            list[position].data
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