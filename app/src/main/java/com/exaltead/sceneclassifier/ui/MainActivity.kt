package com.exaltead.sceneclassifier.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassifierServiceConncetion

private const val TAG = "MainActivity"
private const val RECORD_AUDIO_CODE = 300

class MainActivity : FragmentActivity() {

    private val connection: ClassifierServiceConncetion = ClassifierServiceConncetion(this)
    var infoText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startInfoFragment(this)
    }

    override fun onStart() {
        super.onStart()
        if(checkAndRequestPermission()){
            attemptBindClassificationService(this, connection)
        }
        // SHOW ERROR DIALOG / FRAGMENT

    }

    override fun onStop() {
        super.onStop()
        if(connection.binder != null){
            unbindService(connection)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == RECORD_AUDIO_CODE && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission granted")
            attemptBindClassificationService(this, connection)
        }
        else{
            Log.w(TAG, "Permission not granted")
            infoText = resources.getString(R.string.missing_permission)
        }
    }

    private fun checkAndRequestPermission():Boolean{
        return if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission is not yet granted")

            // TODO: show rationale: https://developer.android.com/training/permissions/requesting.html

            ActivityCompat.requestPermissions(this, Array(1,
                    { _ -> Manifest.permission.RECORD_AUDIO}), RECORD_AUDIO_CODE)
            false
        }
        else{
            Log.i(TAG, "Permission already granted")
            true
        }
    }

    fun getClassificationService(): ClassifiationService?{
        return connection.binder?.service
    }

    fun notifyServiceReady(){
        startClassificationFragment(this)
    }
    private fun attemptBindClassificationService(activity: Activity, connection: ClassifierServiceConncetion){
        infoText = resources.getString(R.string.binding_service)
        when (activity.bindService(Intent(activity, ClassifiationService::class.java), connection, Context.BIND_AUTO_CREATE)){
            true -> Log.i(TAG, "Service successfully bound")
            false -> Log.i(TAG, "Binding service failed")
        }
    }
}

private fun startClassificationFragment(activity: FragmentActivity){
    activity.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ClassificationFragment())
            .commit()
}
private fun startInfoFragment(activity: FragmentActivity){
    activity.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, InformationFragment())
            .commit()
}

