package com.artevak.kasirpos.ui.activity.item.detail

import com.artevak.kasirpos.data.repository.ItemRepository

class DetailBarangUseCase(val itemRepo : ItemRepository) {
    fun deleteItem(key : String) = itemRepo.deleteItem(key)
}