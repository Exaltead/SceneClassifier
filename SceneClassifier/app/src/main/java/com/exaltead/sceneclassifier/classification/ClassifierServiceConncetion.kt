package com.exaltead.sceneclassifier.classification

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.exaltead.sceneclassifier.ui.MainActivity

private const val TAG: String = "ClassifierServiceConncetion"

class ClassifierServiceConncetion(val activity: MainActivity) : ServiceConnection{

    var binder: ClassifierBinder? = null
    override fun onServiceDisconnected(p0: ComponentName?) {
        Log.i(TAG, "Service disconnected")
        binder = null
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        if(p1 is ClassifierBinder){
            Log.i(TAG, "Service connected")
            binder = p1
        }
        else{
            Log.i(TAG, "Service attempted to connect")
        }
    }

}