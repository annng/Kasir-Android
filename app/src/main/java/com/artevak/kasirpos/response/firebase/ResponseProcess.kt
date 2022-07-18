package com.artevak.kasirpos.response.firebase

data class ResponseProcess<ITEM : Any?>(var data : ITEM, var status : StatusRequest)