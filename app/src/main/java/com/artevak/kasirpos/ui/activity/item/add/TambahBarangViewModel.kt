package com.artevak.kasirpos.ui.activity.item.add

import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.base.BaseViewModel
import com.artevak.kasirpos.common.const.Cons
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseProcess
import com.artevak.kasirpos.response.firebase.StatusRequest
import me.shaohui.advancedluban.Luban
import me.shaohui.advancedluban.OnCompressListener
import retrofit2.Response
import java.io.File

class TambahBarangViewModel(private val useCase : TambahBarangUseCase) : BaseViewModel() {

    val _addItem = MutableLiveData<ResponseProcess<String>>()
    val addItem = _addItem

    var obsCompressImage = MutableLiveData<ResponseProcess<File?>>()
    fun compressImage(file: File) {
        obsCompressImage.postValue(
            ResponseProcess(
                data = null,
                status = StatusRequest.LOADING
            )
        )
        Luban.compress(
            file,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        )
            .setMaxSize(Cons.IMAGE.LIMIT_IMAGE_SIZE)
            .setMaxWidth(Cons.IMAGE.LIMIT_IMAGE_DIMEN)
            .setMaxHeight(Cons.IMAGE.LIMIT_IMAGE_DIMEN)
            .setCompressFormat(Bitmap.CompressFormat.JPEG)
            .putGear(Luban.CUSTOM_GEAR)
            .launch(object : OnCompressListener {
                override fun onSuccess(file: File?) {
                    file?.let {
                        obsCompressImage.postValue(
                            ResponseProcess(
                                data = it,
                                status = StatusRequest.SUCCESS
                            )
                        )
                    }
                }

                override fun onError(e: Throwable?) {
                    obsCompressImage.postValue(
                        ResponseProcess(
                            data = null,
                            status = StatusRequest.ERROR
                        )
                    )
                }

                override fun onStart() {

                }
            })
    }

    fun addItem(item : Barang){
        useCase.addItem(item, _addItem)
    }
}