package com.artevak.kasirpos.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.artevak.lokamotor.base.BaseActivity
import com.artevak.kasirpos.databinding.ActivityTambahBarangBinding
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.response.CommonResponse
import com.artevak.kasirpos.util.ApiMain
import com.artevak.kasirpos.util.PermissionHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.ArrayList

class TambahBarangActivity : BaseActivity(),PermissionHelper.PermissionListener {
    lateinit var binding : ActivityTambahBarangBinding

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    lateinit var bos : ByteArrayOutputStream
    private var fileUri: Uri? = null
    lateinit var imagename: MultipartBody.Part

    var TAG_SAVE_BARANG = "saveBarang"
    var TAG_ADD_FOTO = "addFoto"
    var TAG_GET_SATUAN = "satuan"
    var selectedSatuan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mUserPref = UserPreference(this)

        permissionHelper = PermissionHelper(this)
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
        binding.spSatuan.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedSatuan = binding.spSatuan.getItemAtPosition(position).toString()
                Log.d(TAG_GET_SATUAN,"nama satuan : "+ binding.spSatuan.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        selectedSatuan = binding.spSatuan.getItemAtPosition(0).toString()


    }

    fun checkValidation(){
        val nama_barang = binding.etNamaBarang.text.toString()
        val harga_beli = binding.etHargaBeli.text.toString()
        val harga_jual = binding.etHargaJual.text.toString()
        val stok_awal = binding.etStokAwal.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()

        if (selectedSatuan.equals("")){
            showErrorMessage("Anda belum memilih satuan")
        }else if (nama_barang.equals("") || nama_barang.length == 0){
            showErrorMessage("Nama barang belum diisi")
        }else if (harga_beli.equals("") || harga_beli.length == 0){
            showErrorMessage("Harga beli belum diisi")
        }else if (harga_jual.equals("") || harga_jual.length == 0){
            showErrorMessage("harga jual belum diisi")
        }else if (stok_awal.equals("") || stok_awal.length == 0){
            showErrorMessage("stok awal belum diisi")
        }else if (deskripsi.equals("") || deskripsi.length == 0){
            showErrorMessage("deskripsi belum diisi")
        }else if (fotoBitmap == null){
            showErrorMessage("Foto barang belum dipilih")
        }else{
            saveBarang(nama_barang,harga_beli,harga_jual,stok_awal,deskripsi,selectedSatuan)
        }
    }

    fun saveBarang(
        name : String,
        harga_beli : String,
        harga_jual : String,
        stok : String,
        deskripsi : String,
        satuan : String,
    ){

        showLoading(this)
        val key_picture = "picture"

        val builder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart(key_picture,"fotoupload.bmp",RequestBody.create(MultipartBody.FORM,bos.toByteArray()))
        builder.addFormDataPart("name",name)
        builder.addFormDataPart("harga_beli",harga_beli)
        builder.addFormDataPart("harga_jual",harga_jual)
        builder.addFormDataPart("stok",stok)
        builder.addFormDataPart("deskripsi",deskripsi)
        builder.addFormDataPart("satuan",satuan)
        val requestBody: RequestBody = builder.build()

        ApiMain().services.addBarang(mUserPref.getToken(),requestBody).enqueue(
            object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    showErrorMessage("gagal melakukan simpan data, coba lagi nanti")
                    Log.d(TAG_SAVE_BARANG,t.message.toString())
                    dismissLoading()
                }
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    val updateResponse = response.body()
                    val responseInfo = response.code()
                    Log.d(TAG_SAVE_BARANG,"body "+updateResponse!!.toString())
                    Log.d(TAG_SAVE_BARANG,"code "+responseInfo.toString())

                    dismissLoading()
                    if(response.code() == 200) {
                        showSuccessMessage("Tambah barang berhasil")
                        onBackPressed()

                    }else if (response.code() == 202){
                        showErrorMessage("gagal melakukan penambahan data, coba lagi nanti")
                    }else if (response.code() == 401){
                        showErrorMessage("terjadi error pada token, login kembali..")
                        logout()
                        val i = Intent(this@TambahBarangActivity, SplashActivity::class.java)
                        startActivity(i)
                    }
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){

            if(data.data == null){
                return
            }

            filePath = data.data
            fileUri = data.data
            Log.d(TAG_ADD_FOTO,"filepath : "+filePath.toString())

            // membuat variable yang menampung path dari picked image.
            //val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path

            // membuat request body yang berisi file dari picked image.
            val requestBody = RequestBody.create("multipart".toMediaTypeOrNull(), File(filePath.toString()))

            // mengoper value dari requestbody sekaligus membuat form data untuk upload. dan juga mengambil nama dari picked image
            imagename = MultipartBody.Part.createFormData("imagename",
                File(filePath.toString())?.name,requestBody)
            Log.d(TAG_ADD_FOTO,"imagename : "+imagename)

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,30,bos)
                fotoBitmap = bitmap
                binding.ivBarang.setImageBitmap(fotoBitmap)

            } catch (e: IOException) {
                e.printStackTrace()
                showErrorMessage("terjadi kesalahan, coba lagi nanti")
            }


        }



    }

    fun checkPermission(){
        val listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    fun launchGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onPermissionCheckDone() {
        launchGallery()
    }
}