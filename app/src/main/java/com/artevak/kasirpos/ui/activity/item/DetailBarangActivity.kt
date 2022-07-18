package com.artevak.kasirpos.ui.activity.item

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityDetailBarangBinding
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.UserPreference
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailBarangActivity : BaseActivity() {
    lateinit var binding : ActivityDetailBarangBinding
    lateinit var i : Intent
    lateinit var barang : Barang

    var TAG_DELETE = "deletebarang"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        i = intent
        barang = i.getParcelableExtra("barang")!!

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnEdit.setOnClickListener {
            val i = Intent(this, UbahBarangActivity::class.java)
            i.putExtra("barang",barang)
            startActivity(i)
        }
        binding.btnDelete.setOnClickListener {
            deleteBarang(barang.id!!)
        }

        updateUI()
    }

    fun updateUI(){
        val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
        val df = nf as DecimalFormat

        val imageURl = barang.picture
        Glide.with(this)
            .load(imageURl)
            .into(binding.ivFoto)

        binding.tvNamaBarang.setText(barang.name)
        binding.tvStok.setText(""+barang.stok)
        binding.tvDeskripsi.setText(barang.deskripsi)
        binding.tvSatuan.setText(barang.satuan)
        binding.tvHargaBeli.setText(""+df.format(barang.harga_beli))
        binding.tvHargaJual.setText(""+df.format(barang.harga_jual))
    }

    fun deleteBarang(id_barang : String){
        //TODO delete barang
        Toast.makeText(this, "Delete Barang", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }
}