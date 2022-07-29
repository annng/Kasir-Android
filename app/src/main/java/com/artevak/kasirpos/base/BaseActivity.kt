package com.artevak.kasirpos.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.artevak.kasirpos.R
import com.artevak.kasirpos.common.listener.ActivityListener
import com.artevak.kasirpos.common.util.FileUtil
import com.artevak.kasirpos.common.util.PermissionHelper
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.SimpleDateFormat


open class BaseActivity : AppCompatActivity(), ActivityListener {

    lateinit var permissionHelper: PermissionHelper

    lateinit var pDialogLoading: SweetAlertDialog

    private var activityLauncherCallback: ((ActivityResult) -> Unit)? = null
    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            activityLauncherCallback?.invoke(it)
        }
    private var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                handlePickedData(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionHelper(this)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)

        pDialogLoading = SweetAlertDialog(applicationContext, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)


    }

    open fun showLoading(mcontext: Context) {
        pDialogLoading = SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)

        pDialogLoading.show()
    }

    fun dismissLoading() {
        pDialogLoading.dismiss()
    }

    open fun initUI() {

    }

    open fun initListener() {

    }

    open fun setUI() {

    }

    open fun initAdapter() {

    }


    fun showSweetInfo(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Informasi")
            .setContentText(message)
            .show()
    }

    fun showErrorMessage(message: String) {
        applicationContext?.let { Toasty.error(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showSuccessMessage(message: String) {
        applicationContext?.let { Toasty.success(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showLongSuccessMessage(message: String) {
        applicationContext?.let { Toasty.success(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showLongErrorMessage(message: String) {
        applicationContext?.let { Toasty.error(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showInfoMessage(message: String) {
        applicationContext?.let { Toasty.info(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showWarningMessage(message: String) {
        applicationContext?.let { Toasty.warning(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun convertDate(tanggal: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }

    fun startActivity(intent: Intent, result: (ActivityResult) -> Unit) {
        activityLauncherCallback = result
        activityLauncher.launch(intent)
    }

    override fun onStart() {
        super.onStart()

    }

    private fun openGallery() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }

        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)

        galleryLauncher.launch(intent)
    }


    protected fun pickPhoto() {
        openGallery()
    }

    private fun handlePickedData(dataIntent: Intent?) {
        dataIntent?.let { data ->
            if (data.clipData != null) {
                val dataClipCount = data.clipData!!.itemCount
                for (i in 0 until dataClipCount) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    setImageFilePath(imageUri)
                }
            } else {
                val dataUri = dataIntent.data
                dataUri?.let { setImageFilePath(it) }
            }
        }
    }

    private fun setImageFilePath(uri: Uri) {
        val selectedPath = FileUtil.getRealPath(this, uri)
        if (selectedPath != null) {
            onPickFile(File(selectedPath))
        } else {
            Toast.makeText(
                this,
                getString(R.string.label_file_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onPickFile(file: File?) {

    }

}