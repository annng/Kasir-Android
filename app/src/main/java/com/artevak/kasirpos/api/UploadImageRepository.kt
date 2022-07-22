package com.artevak.kasirpos.api

import android.content.Context
import android.net.Uri
import com.uploadcare.android.library.api.UploadcareClient
import com.uploadcare.android.library.api.UploadcareFile
import com.uploadcare.android.library.callbacks.UploadFileCallback
import com.uploadcare.android.library.exceptions.UploadcareApiException
import com.uploadcare.android.library.upload.FileUploader
import java.io.File

class UploadImageRepository(val context: Context) {

    object ApiKeys {
        /**
         * Fill in your imgur client id below. Don't have a client id? Get one here: https://api.imgur.com/oauth2/addclient
         */
        const val PUBLIC = "06a3dbebef332c6220d2"
        const val SECRET = "c283d1efcd58da45c9b6"
    }

    fun uploading(
        fileUri: File,
        onProgress: (OnProccess) -> Unit,
        onFailure: (String) -> Unit,
        onSuccess: (UploadcareFile) -> Unit
    ) {
        val uploadcare = UploadcareClient(ApiKeys.PUBLIC, ApiKeys.SECRET)

        val uploader = FileUploader(
            uploadcare,
            fileUri
        ) // Use "MultipleFilesUploader" for multiple files.
            .store(true)
        // Other upload parameters.

        uploader.uploadAsync(object : UploadFileCallback {
            override fun onFailure(e: UploadcareApiException) {
                e.message?.let { onFailure(it) }
            }

            override fun onProgressUpdate(
                bytesWritten: Long,
                contentLength: Long,
                progress: Double
            ) {
                onProgress(OnProccess().apply {
                    this.byteWritten = byteWritten
                    this.contentLength = contentLength
                    this.progress = progress
                })
                // Upload progress info.
            }

            override fun onSuccess(result: UploadcareFile) {
                // Successfully uploaded file to Uploadcare.
                onSuccess(result)
            }
        })
    }
}

class OnProccess {
    var byteWritten: Long = 0L
    var contentLength: Long = 0L
    var progress: Double = 0.0
}