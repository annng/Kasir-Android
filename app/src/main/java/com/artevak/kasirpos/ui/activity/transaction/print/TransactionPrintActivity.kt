package com.artevak.kasirpos.ui.activity.transaction.print

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.application.isradeleon.thermalprinter.ConnectBluetoothActivity
import com.application.isradeleon.thermalprinter.models.PrintAlignment
import com.application.isradeleon.thermalprinter.models.PrintFont
import com.application.isradeleon.thermalprinter.models.ThermalPrinter
import com.artevak.kasirpos.R
import com.artevak.kasirpos.base.BaseActivity
import com.artevak.kasirpos.common.util.PermissionHelper

class TransactionPrintActivity : BaseActivity(), PermissionHelper.PermissionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_print)

        permissionHelper.setPermissionListener(this)
        checkPermission()
    }

    private fun gotoBluetooth() {
        startActivityForResult(
            Intent(this, ConnectBluetoothActivity::class.java),
            ConnectBluetoothActivity.CONNECT_BLUETOOTH
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ConnectBluetoothActivity.CONNECT_BLUETOOTH) {
            // ThermalPrinter is ready
            val print = ThermalPrinter.instance

            //header
            print.write("Hello world", PrintAlignment.CENTER, PrintFont.LARGE)
                .fillLineWith('-')

            //price
            print.write("Price", "0.5 USD")

            //items
            for (i in 0 until 3) {
                print.write("You Buy ($i)", "\$ ${(i + 3)}.00")
            }

            print.print()


        }
    }


    fun checkPermission() {
        val listPermissions: MutableList<String> = java.util.ArrayList()
        listPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        listPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    override fun onPermissionCheckDone() {
        gotoBluetooth()
    }
}