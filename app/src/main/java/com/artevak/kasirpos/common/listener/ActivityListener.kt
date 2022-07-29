package com.artevak.kasirpos.common.listener

import java.io.File

/**
 * BaseActivity 功能监听
 */
interface ActivityListener {
    fun onPickFile(file : File?)
}