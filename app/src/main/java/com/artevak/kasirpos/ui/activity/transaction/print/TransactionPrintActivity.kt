package com.artevak.kasirpos.ui.activity.transaction.print

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.application.isradeleon.thermalprinter.ConnectBluetoothActivity
import com.application.isradeleon.thermalprinter.models.PrintAlignment
import com.application.isradeleon.thermalprinter.models.PrintFont
import com.application.isradeleon.thermalprinter.models.ThermalPrinter
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.util.PermissionHelper

class TransactionPrintActivity : BaseActivity(), PermissionHelper.PermissionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_print)

        permissionHelper.setPermissionListener(this)
        checkPermission()
    }

    private fun gotoBluetooth(){
        startActivityForResult(
            Intent(this, ConnectBluetoothActivity::class.java),
            ConnectBluetoothActivity.CONNECT_BLUETOOTH
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == ConnectBluetoothActivity.CONNECT_BLUETOOTH){
            // ThermalPrinter is ready
            ThermalPrinter.instance
                .write("Hello world", PrintAlignment.CENTER, PrintFont.LARGE)
                .fillLineWith('-')
                .write("Let's eat","some tacos")
                .write("Price","0.5 USD")
                .writeImage(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_account_circle_24))
                .print()
        }
    }


    fun checkPermission(){
        val listPermissions: MutableList<String> = java.util.ArrayList()
        listPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        listPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    override fun onPermissionCheckDone() {
        gotoBluetooth()
    }
}