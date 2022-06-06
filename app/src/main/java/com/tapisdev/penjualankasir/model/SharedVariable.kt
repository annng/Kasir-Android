package com.tapisdev.penjualankasir.model


class SharedVariable {

    companion object {
        lateinit var user : UserModel
        var nextFragment = ""
        var arrLayananDipilih = ArrayList<String>()

        open fun resetLayananDipilih(){
            arrLayananDipilih.clear()
        }
    }


}