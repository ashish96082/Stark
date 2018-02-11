package com.example.piyush.magicalmethods.listeners

import com.beust.klaxon.JsonObject

/**
 * Created by ashish kumar on 04-11-2017.
 */
open class MMfromServerListener {
    open fun onDownloadComplete(json: JsonObject) {}
    open fun onPreDownload() {}
}