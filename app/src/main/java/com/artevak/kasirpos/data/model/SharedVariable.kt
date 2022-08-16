package com.artevak.kasirpos.data.model


class SharedVariable {

    companion object {
        lateinit var user : User
        var nextFragment = ""
        var arrLayananDipilih = ArrayList<String>()
        var selectedCustomer: Customer? = null
        var pelangganType = "guest" //defaultnya guest

        open fun resetLayananDipilih(){
            arrLayananDipilih.clear()
        }
    }


}