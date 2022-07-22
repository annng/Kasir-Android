package com.artevak.kasirpos.data.model

class Menu {
    var resIcon : Int = 0
    var title : String = ""
    var isEnable : Boolean = true

    constructor(){

    }
    constructor(resIcon: Int, title: String) {
        this.resIcon = resIcon
        this.title = title
    }
    constructor(resIcon: Int, title: String, isEnable : Boolean) {
        this.resIcon = resIcon
        this.title = title
        this.isEnable = isEnable
    }
}
