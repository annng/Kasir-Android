package com.artevak.kasirpos.ui.activity.item.edit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.artevak.kasirpos.R
import com.artevak.kasirpos.api.UploadImageRepository
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.const.Cons
import com.artevak.kasirpos.common.util.PermissionHelper
import com.artevak.kasirpos.common.util.ext.dashIfEmpty
import com.artevak.kasirpos.common.util.ext.loadImageCenterCrop
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.databinding.ActivityUbahBarangBinding
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_ubah_barang.*
import okhttp3.MultipartBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class UbahBarangActivity : BaseActivity(), PermissionHelper.PermissionListener {

    lateinit var i: Intent
    lateinit var barang: ResponseData<Barang>
    lateinit var binding: ActivityUbahBarangBinding

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    var fotoBitmap: Bitmap? = null
    lateinit var bos: ByteArrayOutputStream
    private var fileUri: Uri? = null
    lateinit var imagename: MultipartBody.Part

    var fotoUrl = ""
    var selectedSatuan = ""
    var TAG_UPDATE_BARANG = "saveBarang"
    var TAG_ADD_FOTO = "addFoto"
    var TAG_GET_SATUAN = "satuan"
    var selectedPosSatuan = 0
    var listSatuan = ArrayList<String>()
    private val viewModel: UbahBarangViewModel by viewModel()

    companion object {
        fun generateIntent(context: Context, item: Barang, key: String): Intent {
            val intent = Intent(context, UbahBarangActivity::class.java)
            intent.putExtra(Cons.EXTRA.KEY_ITEM, item)
            intent.putExtra(Cons.EXTRA.KEY, key)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        permissionHelper.setPermissionListener(this)

        i = intent
        val key = i.getStringExtra(Cons.EXTRA.KEY)
        val item = i.getParcelableExtra<Barang>(Cons.EXTRA.KEY_ITEM)
        item?.let {
            barang = ResponseData(it, key ?: "")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.spSatuan.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedSatuan = binding.spSatuan.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        binding.ivBarang.setOnClickListener {
            checkPermission()
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }

        updateUI()

        observeData()

    }

    private fun observeData() {
        viewModel.obsCompressImage.observe(this) {
            when (it.status) {
                StatusRequest.SUCCESS -> {
                    it.data?.let { it1 -> uploadImage(it1) }
                }
                StatusRequest.ERROR, StatusRequest.FAILED -> showErrorMessage(getString(R.string.error_toast_failed_upload))
                else -> showLoading(this)
            }
        }
    }

    fun checkValidation() {
        val nama_barang = binding.etNamaBarang.text
        val harga_beli = binding.etHargaBeli.rawValue
        val harga_jual = binding.etHargaJual.rawValue
        val stok_awal = binding.etStokAwal.text
        val deskripsi = binding.etDeskripsi.text

        if (selectedSatuan.equals("")) {
            showErrorMessage("Anda belum memilih satuan")
        } else if (nama_barang.equals("") || nama_barang.length == 0) {
            showErrorMessage("Nama barang belum diisi")
        } else if (etHargaBeli.text.isEmpty()) {
            showErrorMessage("Harga beli belum diisi")
        } else if (etHargaJual.text.isEmpty()) {
            showErrorMessage("harga jual belum diisi")
        } else if (stok_awal.equals("") || stok_awal.length == 0) {
            showErrorMessage("stok awal belum diisi")
        } else if (deskripsi.equals("") || deskripsi.length == 0) {
            showErrorMessage("deskripsi belum diisi")
        } else {
            val item = Barang(
                nama_barang,
                harga_beli,
                harga_jual,
                stok_awal.toInt(),
                deskripsi,
                fotoUrl,
                selectedSatuan
            )
            updateBarang(item)
        }
    }

    fun updateBarang(
        item: Barang
    ) {
        viewModel.updateItem(barang.keys, item)
        Toast.makeText(this, "Update barang", Toast.LENGTH_SHORT).show()
        onBackPressed()

    }

    fun setArraySatuan() {
        listSatuan.add("Pcs")
        listSatuan.add("Kg")
        listSatuan.add("Gram")
        listSatuan.add("Ons")
        listSatuan.add("Dus")
        listSatuan.add("Lusin")

        var tempSatuan = ""
        for (i in 0 until listSatuan.size) {
            tempSatuan = listSatuan.get(i)
            if (tempSatuan == selectedSatuan) {
                selectedPosSatuan = i
            }
        }
        binding.spSatuan.setSelection(selectedPosSatuan)
    }

    fun updateUI() {
        val nf = NumberFormat.getNumberInstance(Locale.getDefault())
        val df = nf as DecimalFormat

        fotoUrl = barang.data.picture ?: ""
        selectedSatuan = barang.data.satuan!!

        val imageURl = barang.data.picture
        Glide.with(this)
            .load(imageURl)
            .into(binding.ivBarang)

        binding.etNamaBarang.text = barang.data.name.dashIfEmpty()
        binding.etStokAwal.text = barang.data.stok.toString()
        binding.etDeskripsi.text = barang.data.deskripsi.dashIfEmpty()
        binding.etHargaBeli.setText("" + barang.data.harga_beli)
        binding.etHargaJual.setText("" + barang.data.harga_jual)

        setArraySatuan()
    }

    fun checkPermission() {
        val listPermissions: MutableList<String> = java.util.ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    private fun uploadImage(file: File) {
        showLoading(this)
        UploadImageRepository(this).uploading(
            file,
            {

            },
            {
                dismissLoading()
                Toast.makeText(
                    this,
                    "Failed uploading file. please try again. $it",
                    Toast.LENGTH_SHORT
                )
                    .show()
            },
            {
                dismissLoading()
                fotoUrl = it.originalFileUrl.toString()
                binding.ivBarang.loadImageCenterCrop(fotoUrl)
            })
    }

    override fun onPickFile(file: File?) {
        if (file != null)
            viewModel.compressImage(file)
        else
            Toast.makeText(this, getString(R.string.label_file_not_found), Toast.LENGTH_SHORT)
                .show()
    }

    override fun onPermissionCheckDone() {
        pickPhoto()
    }
}