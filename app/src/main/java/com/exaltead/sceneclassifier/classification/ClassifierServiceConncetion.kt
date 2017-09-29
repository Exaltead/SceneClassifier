package com.exaltead.sceneclassifier.classification

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
private const val TAG: String = "ClassifierServiceConncetion"

class ClassifierServiceConncetion : ServiceConnection{

    var binder: ClassifierBinder? = null

    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.i(TAG, "Service disconnected")
        binder = null
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        if(p1 is ClassifierBinder){
            Log.i(TAG, "Service bound")
            binder = p1

        }
    }

}