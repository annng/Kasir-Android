package com.artevak.kasirpos.ui.activity.item.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.const.Cons
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.databinding.ActivityDetailBarangBinding
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.ui.activity.item.edit.UbahBarangActivity
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
class DetailBarangActivity : BaseActivity() {
    lateinit var binding: ActivityDetailBarangBinding
    lateinit var i: Intent
    lateinit var barang: ResponseData<Barang>
    private val viewModel : DetailBarangViewModel by viewModel()
    companion object {
        fun generateIntent(context: Context, item: Barang, key: String): Intent {
            val intent = Intent(context, DetailBarangActivity::class.java)
            intent.putExtra(Cons.EXTRA.KEY_ITEM, item)
            intent.putExtra(Cons.EXTRA.KEY, key)

            return intent
        }
    }

    var TAG_DELETE = "deletebarang"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        i = intent
        val item: Barang? = i.getParcelableExtra(Cons.EXTRA.KEY_ITEM)
        val key = i.getStringExtra(Cons.EXTRA.KEY)
        item?.let {
            barang = ResponseData(it, key ?: "")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnEdit.setOnClickListener {
            val i = UbahBarangActivity.generateIntent(this, barang.data, barang.keys)
            startActivity(i)
        }
        binding.btnDelete.setOnClickListener {
            deleteBarang()
        }

        updateUI()
    }

    fun updateUI() {
        val nf = NumberFormat.getNumberInstance(Locale.getDefault())
        val df = nf as DecimalFormat


        val imageURl = barang.data.picture
        Glide.with(this)
            .load(imageURl)
            .into(binding.ivFoto)

        binding.tvNamaBarang.text = barang.data.name
        binding.tvStok.text = "" + barang.data.stok
        binding.tvDeskripsi.text = barang.data.deskripsi
        binding.tvSatuan.text = barang.data.satuan
        binding.tvHargaBeli.text =
             "${df.format(barang.data.harga_beli)} ${Currency.getInstance(Locale.getDefault())}"
        binding.tvHargaJual.text =
            "${df.format(barang.data.harga_jual)} ${Currency.getInstance(Locale.getDefault())}"
    }

    fun deleteBarang() {
        viewModel.deleteItem(barang.keys)
        Toast.makeText(this, "Delete Barang", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }
}