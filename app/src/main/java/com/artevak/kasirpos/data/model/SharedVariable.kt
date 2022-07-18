package com.artevak.kasirpos.data.model


class SharedVariable {

    companion object {
        lateinit var user : UserModel
        var nextFragment = ""
        var arrLayananDipilih = ArrayList<String>()
        var selectedPelanggan: Pelanggan? = null
        var pelangganType = "guest" //defaultnya guest

        open fun resetLayananDipilih(){
            arrLayananDipilih.clear()
        }
    }


}