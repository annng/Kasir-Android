package com.artevak.kasirpos.common.util

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.artevak.kasirpos.BuildConfig
import com.artevak.kasirpos.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


object FileUtil {

    fun openFile(context: Context, uri: Uri) {
        var attachmentUri = uri
        if (ContentResolver.SCHEME_FILE == attachmentUri.scheme) {
            val file = File(attachmentUri.path ?: "")
            attachmentUri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.provider",
                file
            )
        }
        val mimeType = context.contentResolver.getType(attachmentUri)
        val openAttachmentIntent = Intent(Intent.ACTION_VIEW)
        openAttachmentIntent.setDataAndType(attachmentUri, mimeType)
        openAttachmentIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.startActivity(openAttachmentIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.all_file_cannot_opened),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun isFileExistsFromServer(pathUrl: String): Boolean {
        return pathUrl.startsWith("https://")
    }

    fun getFileSizeInMB(path: String): Long {
        val file = File(path)
        val fileSizeInKB = file.length() / 1024
        return fileSizeInKB / 1024
    }

    fun copyFileToTemp(context: Context, path: String): Uri? {
        try {
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val fileName = path.substring(path.lastIndexOf("/") + 1)
            val dir = File.createTempFile("Kerjaan_", fileName, storageDir)
            val file = File(path)
            FileInputStream(file).use { `in` ->
                FileOutputStream(dir).use { out ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while (`in`.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                }
            }
            return dir.toUri()
        } catch (throwable: Throwable) {
            return null
        }
    }

    fun isImage(fileName: String): Boolean {
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(fileName))
        if (type == null) {
            return false
        } else if (type.contains("image")) {
            return true
        }
        return false
    }

    private fun getFileContentUri(context: Context, file: File): Uri? {
        val filePath: String = file.absolutePath
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ",
            arrayOf(filePath),
            null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getInt(
                cursor.getColumnIndex(MediaStore.MediaColumns._ID)
            )
            val baseUri =
                Uri.parse("content://${BuildConfig.APPLICATION_ID}.provider/my_images/")
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            if (file.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    private fun getExtension(fileName: String): String? {
        val lastDotPosition = fileName.lastIndexOf('.')
        var ext = fileName.substring(lastDotPosition + 1)
        ext = ext.replace("_", "")
        return ext.trim().toLowerCase(Locale.getDefault())
    }


    fun getRealPath(context: Context, fileUri: Uri): String? {
        // SDK < API11
        return if (Build.VERSION.SDK_INT < 19) {
            getRealPathFromURIAPI11to18(context, fileUri)
        } else {
            getRealPathFromURIAPI19(context, fileUri)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun getRealPathFromURIAPI11to18(
        context: Context?,
        contentUri: Uri?
    ): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null
        val cursorLoader =
            CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun getRealPathFromURIAPI19(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]

                    // This is for checking Main Memory
                    return if ("primary".equals(type, ignoreCase = true)) {
                        if (split.size > 1) {
                            Environment.getExternalStorageDirectory()
                                .toString() + "/" + split[1]
                        } else {
                            Environment.getExternalStorageDirectory().toString() + "/"
                        }
                        // This is for checking SD Card
                    } else {
                        "storage" + "/" + docId.replace(":", "/")
                    }
                }
                isDownloadsDocument(uri) -> {
                    val fileName =
                        getFilePath(context, uri)
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/Download/" + fileName
                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), id.toLong()
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        null,
                        null
                    )
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    val contentUri: Uri? = when (type) {
                        "image" -> {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                        else -> {
                            MediaStore.Files.getContentUri("external")
                        }
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getFilePath(
        context: Context,
        uri: Uri
    ): String? {
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        context.contentResolver.query(
            uri, projection, null, null,
            null
        ).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}