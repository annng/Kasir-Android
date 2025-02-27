package com.artevak.kasirpos.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raya on 5/4/17.
 */

public class PermissionHelper {

    private List<String> listPermissionsNeeded = new ArrayList<>();
    private List<String> listPermissions = new ArrayList<>();
    private PermissionListener listener;
    private Activity activity;

    private final int REQUEST_PERMISSION = 789;

    private String deniedPermissionMessage = "Pengguna yang terhormat, perizinan ini aman dan dibutuhkan untuk melanjutkan proses ini";
    private String neverAskPermissionMessage = "";

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public void setPermissionListener(PermissionListener listener) {
        this.listener = listener;
    }

    public void checkAndRequestPermissions(List<String> listPermissions) {
        this.listPermissions = listPermissions;
        checkAndRequestPermissions();
    }

    public void setDeniedPermissionMessage(String message) {
        if (message != null)
            deniedPermissionMessage = message;
    }

    public void setNeverAskPermissionMessage(String message) {
        if (message != null)
            neverAskPermissionMessage = message;
    }

    /**
     * Call this to check permission.
     * Will looping for check permission until user Approved it
     */
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listPermissionsNeeded.clear();
            for (String permission : listPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                    listPermissionsNeeded.add(permission);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION);
                return false;
            }
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        listener.onPermissionCheckDone();
        return true;
    }

    /**
     * Handling permission callback after you click deny or ok
     */
    public void onRequestCallBack(int RequestCode, String[] permissions, int[] grantResults) {//2. call this inside onRequestPermissionsResult
        switch (RequestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0) {
                    boolean granted = true;
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            granted = false; //false, if one permission not granted
                    }

                    // all permission granted
                    if (granted)
                        checkAndRequestPermissions();
                    else {
                        // Some permissions are not granted ask again. Ask again explaining the usage of permission.
                        boolean neverAsk = true;
                        for (String permission : listPermissions) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                                neverAsk = false;
                        }
                        if (!neverAsk) {
                            showDialogOK(deniedPermissionMessage,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        else {
                            if (!neverAskPermissionMessage.isEmpty())
                                Toast.makeText(activity, neverAskPermissionMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivityForResult(intent, REQUEST_PERMISSION);
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    public interface PermissionListener {
        void onPermissionCheckDone();
    }

}