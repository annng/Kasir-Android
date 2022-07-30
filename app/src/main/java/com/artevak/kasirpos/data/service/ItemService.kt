package com.artevak.kasirpos.data.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.R
import com.artevak.kasirpos.common.const.DBConst
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.response.firebase.ResponseProcess
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemService(val context: Context, val db: FirebaseDatabase) {
    val itemRef = db.getReference(DBConst.TABLE.ITEM)

    fun addItem(user: String, item: Barang, response: MutableLiveData<ResponseProcess<String>>) {
        response.postValue(
            ResponseProcess(
                context.getString(R.string.info_toast_loading),
                StatusRequest.LOADING
            )
        )

        itemRef.child(user).push().setValue(item)
        response.postValue(
            ResponseProcess(
                context.getString(R.string.info_toast_success_add_item),
                StatusRequest.SUCCESS
            )
        )
    }

    fun getStock(username: String, response: MutableLiveData<ResponseProcess<ArrayList<Barang>?>>) {
        response.postValue(ResponseProcess(null, StatusRequest.LOADING))
        itemRef.child(username).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<Barang>()
                snapshot.children.map {
                    it.getValue(Barang::class.java)?.let { it1 -> items.add(it1) }
                }
                response.postValue(ResponseProcess(items, StatusRequest.SUCCESS))
            }


            override fun onCancelled(error: DatabaseError) {
                response.postValue(ResponseProcess(null, StatusRequest.FAILED, message = context.getString(R.string.error_toast_load_data_error)))
            }
        })
    }
}