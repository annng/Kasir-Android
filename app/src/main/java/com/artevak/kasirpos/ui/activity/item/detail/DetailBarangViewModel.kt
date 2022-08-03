package com.artevak.kasirpos.ui.activity.item.detail

import com.artevak.kasirpos.base.BaseViewModel

class DetailBarangViewModel(val useCase : DetailBarangUseCase) : BaseViewModel(){
    fun deleteItem(key : String) = useCase.deleteItem(key)
}