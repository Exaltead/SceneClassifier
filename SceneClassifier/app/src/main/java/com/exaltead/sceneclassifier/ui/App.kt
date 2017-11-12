package com.exaltead.sceneclassifier.ui

import android.app.Application
import android.content.Context

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        App.context = this.applicationContext

    }

    companion object {
        lateinit var  context: Context
    }

}
