package com.artevak.kasirpos.ui.activity.item.edit

import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.repository.ItemRepository

class UbahBarangUseCase(private val itemRepository: ItemRepository) {

    fun updateItem(key : String, value : Barang) = itemRepository.updateItem(key, value)
}