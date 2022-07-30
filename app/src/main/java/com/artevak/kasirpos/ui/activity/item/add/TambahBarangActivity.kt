package com.artevak.kasirpos.ui.activity.item.add

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.artevak.kasirpos.R
import com.artevak.kasirpos.api.UploadImageRepository
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahBarangBinding
import com.artevak.kasirpos.common.util.PermissionHelper
import com.artevak.kasirpos.common.util.ext.loadImageCenterCrop
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.StatusRequest
import kotlinx.android.synthetic.main.activity_ubah_barang.*
import java.io.File
import java.util.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

class TambahBarangActivity : BaseActivity(), PermissionHelper.PermissionListener {
    lateinit var binding: ActivityTambahBarangBinding

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    val viewModel: TambahBarangViewModel by viewModel()

    var fotoUrl = ""
    private var fileUri: Uri? = null

    var TAG_SAVE_BARANG = "saveBarang"
    var TAG_ADD_FOTO = "addFoto"
    var TAG_GET_SATUAN = "satuan"
    var selectedSatuan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        permissionHelper.setPermissionListener(this)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivBarang.setOnClickListener {
            checkPermission()
        }
        binding.btnSimpan.setOnClickListener {
            checkValidation()
        }
        binding.spSatuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedSatuan = binding.spSatuan.getItemAtPosition(position).toString()
                Log.d(
                    TAG_GET_SATUAN,
                    "nama satuan : " + binding.spSatuan.getItemAtPosition(position).toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        selectedSatuan = binding.spSatuan.getItemAtPosition(0).toString()

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

        viewModel.addItem.observe(this) {
            when (it.status) {
                StatusRequest.LOADING -> showLoading(this)
                StatusRequest.SUCCESS -> {
                    dismissLoading()
                    showInfoMessage(getString(R.string.info_toast_success_add_item))
                    onBackPressed()
                }
                else -> {
                    dismissLoading()
                    showErrorMessage(getString(R.string.error_toast_failed_upload))
                }
            }
        }
    }

    private fun checkValidation() {
        val nama_barang = binding.etNamaBarang.text
        val harga_beli = binding.etHargaBeli.rawValue
        val harga_jual = binding.etHargaJual.rawValue
        val stok_awal = binding.etStokAwal.text
        val deskripsi = binding.etDeskripsi.text

        if (selectedSatuan.equals("")) {
            showErrorMessage("Please select unit type")
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
            saveBarang(item)
        }
    }

    fun saveBarang(
        item: Barang
    ) {
        viewModel.addItem(item)

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

    fun checkPermission() {
        val listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
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