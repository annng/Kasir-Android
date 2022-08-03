package com.artevak.kasirpos.data.service

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.artevak.kasirpos.R
import com.artevak.kasirpos.common.const.DBConst
import com.artevak.kasirpos.data.model.Barang
import com.artevak.kasirpos.data.model.shared.SharedPref
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.ResponseProcess
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemService(val context: Context, val db: FirebaseDatabase,
                  private val sharedPref: SharedPref) {
    val itemRef = db.getReference(DBConst.TABLE.ITEM)

    fun addItem(item: Barang, response: MutableLiveData<ResponseProcess<String>>) {
        response.postValue(
            ResponseProcess(
                context.getString(R.string.info_toast_loading),
                StatusRequest.LOADING
            )
        )

        itemRef.child(sharedPref.getUsername()).push().setValue(item)
        response.postValue(
            ResponseProcess(
                context.getString(R.string.info_toast_success_add_item),
                StatusRequest.SUCCESS
            )
        )
    }

    fun updateItem(item : Barang, key : String){
        itemRef.child(sharedPref.getUsername()).child(key).setValue(item)
    }

    fun deleteItem(key : String){
        itemRef.child(sharedPref.getUsername()).child(key).removeValue()
    }

    fun getStock(response: MutableLiveData<ResponseProcess<ArrayList<ResponseData<Barang>>?>>) {
        response.postValue(ResponseProcess(null, StatusRequest.LOADING))
        itemRef.child(sharedPref.getUsername()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = ArrayList<ResponseData<Barang>>()
                snapshot.children.map {
                    it.getValue(Barang::class.java)?.let { it1 ->
                        val data = ResponseData(it1, it.key ?: "")
                        items.add(data)
                    }
                }
                response.postValue(ResponseProcess(items, StatusRequest.SUCCESS))
            }


            override fun onCancelled(error: DatabaseError) {
                response.postValue(ResponseProcess(null, StatusRequest.FAILED, message = context.getString(R.string.error_toast_load_data_error)))
            }
        })
    }
}