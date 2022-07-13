package com.artevak.kasirpos.base

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.artevak.kasirpos.model.UserPreference
import com.artevak.kasirpos.util.PermissionHelper
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat


open class BaseActivity : AppCompatActivity() {

    lateinit var  permissionHelper : PermissionHelper

    lateinit var pDialogLoading : SweetAlertDialog
    lateinit var mUserPref : UserPreference

    private var activityLauncherCallback: ((ActivityResult) -> Unit)? = null
    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        activityLauncherCallback?.invoke(it)
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

    open fun showLoading(mcontext : Context){
        pDialogLoading = SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)

        pDialogLoading.show()
    }

    fun dismissLoading(){
        pDialogLoading.dismiss()
    }



    fun showSweetInfo(message : String){
        SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Informasi")
            .setContentText(message)
            .show()
    }

    fun showErrorMessage(message : String){
        applicationContext?.let { Toasty.error(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showSuccessMessage(message : String){
        applicationContext?.let { Toasty.success(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showLongSuccessMessage(message : String){
        applicationContext?.let { Toasty.success(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showLongErrorMessage(message : String){
        applicationContext?.let { Toasty.error(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showInfoMessage(message : String){
        applicationContext?.let { Toasty.info(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showWarningMessage(message : String){
        applicationContext?.let { Toasty.warning(it, message, Toast.LENGTH_SHORT, true).show() }
    }


    fun logout(){
        mUserPref.saveName("")
        mUserPref.saveNIK("")
        mUserPref.saveEmail("")
        mUserPref.saveJenisUser("")
        mUserPref.savePhone("")
        mUserPref.saveAvatar("")
        mUserPref.saveUsername("")
        mUserPref.saveToken("")
    }

    fun convertDate(tanggal : String): String {
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


}