package com.artevak.kasirpos.common.util.ext

import android.util.Log
import com.artevak.kasirpos.response.firebase.ResponseData
import com.artevak.kasirpos.response.firebase.StatusRequest
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlin.collections.ArrayList

inline fun <reified ITEM : Any> FirebaseDatabase.getDatas(
    tableRef: String,
    crossinline results: (ArrayList<ResponseData<ITEM>>) -> Unit
) {
    val table = this.getReference(tableRef)

    val items: ArrayList<ResponseData<ITEM>> = ArrayList()

    val listener = table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            items.clear()
            for (brandSnap in dataSnapshot.children) {
                val item = brandSnap.value
                val json = Gson().toJson(item).toString()
                brandSnap.key?.let {
                    items.add(ResponseData(json.getObject(), it))
                }

            }

            val sorted = items.sortedBy { it.data.toString() }
            val resArr = ArrayList<ResponseData<ITEM>>()
            resArr.addAll(sorted)
            results(items)
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value
            Log.w("onCancelled", "Failed to read value.", error.toException())
        }


    })

    table.addValueEventListener(listener)

}

inline fun <reified ITEM : Any> FirebaseDatabase.getData(
    tableRef: String,
    equal: String,
    crossinline results: (ResponseData<ITEM>) -> Unit
) {
    var table: Query = this.getReference(tableRef).orderByKey().equalTo("$equal")
    var item: ResponseData<ITEM>
    table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
            if (dataSnapshot.exists()) {
                for (brandSnap in dataSnapshot.children) {
                    val dataSnap = brandSnap.value
                    val json = Gson().toJson(dataSnap).toString()
                    brandSnap.key?.let {
                        item = ResponseData(json.getObject(), it)
                        results(item)
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value

        }
    })
}

fun FirebaseDatabase.isExist(
    tableRef: String,
    equal: String,
    isSuccess: (StatusRequest) -> Unit
) {
    var table: Query = this.getReference(tableRef).orderByKey().equalTo("$equal")
    table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
            if (dataSnapshot.exists()) {
                isSuccess(StatusRequest.SUCCESS)
            } else {
                isSuccess(StatusRequest.FAILED)
            }
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value
            isSuccess(StatusRequest.ERROR)
        }
    })
}

inline fun <reified ITEM : Any> FirebaseDatabase.getDatas(
    tableRef: String,
    field: String,
    equal: String,
    crossinline results: (ArrayList<ResponseData<ITEM>>) -> Unit
) {
    var table: Query = this.getReference(tableRef)
    val items: ArrayList<ResponseData<ITEM>> = ArrayList()
    table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
            if (dataSnapshot.exists()) {
                for (brandSnap in dataSnapshot.children) {
                    val dataSnap = brandSnap.value
                    val json = Gson().toJson(dataSnap).toString()
                    if (brandSnap.child(field).value!! == equal) {
                        brandSnap.key?.let {
                            items.add(ResponseData(json.getObject(), it))
                        }
                    }
                }

                results(items)
            }
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value

        }
    })
}


inline fun <reified ITEM : Any> FirebaseDatabase.getAllData(
    tableRef: String,
    crossinline results: (ArrayList<ResponseData<ITEM>>) -> Unit,
    crossinline status: (StatusRequest) -> Unit,

    ) {
    var table: Query = this.getReference(tableRef)
    val items: ArrayList<ResponseData<ITEM>> = ArrayList()
    table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
            if (dataSnapshot.exists()) {
                status(StatusRequest.SUCCESS)
                for (brandSnap in dataSnapshot.children) {
                    val dataSnap = brandSnap.value
                    val json = Gson().toJson(dataSnap).toString()
                    brandSnap.key?.let {
                        items.add(ResponseData(json.getObject(), it))
                    }
                }
                results(items)
            } else {
                status(StatusRequest.FAILED)
            }
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value
            status(StatusRequest.ERROR)
        }
    })
}

inline fun <reified ITEM> FirebaseDatabase.getData(
    tableRef: String,
    crossinline results: (ITEM) -> Unit
) {
    val table = this.getReference(tableRef)

    var item: ITEM
    table.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) { // This method is called once with the initial value and again
            if (dataSnapshot.exists()) {
                for (brandSnap in dataSnapshot.children) {
                    val dataSnap = brandSnap.value
                    val json = Gson().toJson(dataSnap).toString()
                    item = json.getObject()
                    results(item)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) { // Failed to read value

        }
    })
}

inline fun <reified ITEM> FirebaseDatabase.add(tableRef: String, item: ITEM) {
    this.getReference(tableRef).push().setValue(item)
}

fun FirebaseDatabase.add(tableRef: String, key: (DatabaseReference) -> Unit) {
    val keys = this.getReference(tableRef).push()
    key(keys)
}


fun FirebaseDatabase.addCustomKey(
    tableRef: String,
    key: String,
    item: Any,
    status: (StatusRequest) -> Unit
) {
    this.getReference(tableRef).child(key).setValue(item).addOnSuccessListener {
        status(StatusRequest.SUCCESS)
    }.addOnFailureListener {
        status(StatusRequest.FAILED)
    }
}

inline fun <reified ITEM> FirebaseDatabase.updateData(
    tableRef: String,
    keys: String,
    value: ITEM
) {
    this.getReference(tableRef).child(keys).setValue(value)
}

fun FirebaseDatabase.deleteData(
    tableRef: String,
    keys: String,
) {
    this.getReference(tableRef).child(keys).removeValue()
}