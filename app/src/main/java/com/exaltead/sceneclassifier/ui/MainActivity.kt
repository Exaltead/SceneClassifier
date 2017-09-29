package com.exaltead.sceneclassifier.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassifierServiceConncetion
import kotlinx.android.synthetic.main.activity_main.*
private const val TAG = "MainActivity"
private const val RECORD_AUDIO_CODE = 300

class MainActivity : Activity() {

    private val connection: ClassifierServiceConncetion = ClassifierServiceConncetion()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        if(checkAndRequestPermission()){
            activateClassificationWithFragment(this, connection)
        }
        // SHOW ERROR DIALOG / FRAGMENT

    }

    override fun onStop() {
        super.onStop()
        if(connection.binder != null){
            unbindService(connection)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if(requestCode == RECORD_AUDIO_CODE && grantResults?.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission granted")
            activateClassificationWithFragment(this, connection)
        }
        else{
            Log.w(TAG, "Permission not granted")
            // SHOW ERROR
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
}

private fun activateClassificationWithFragment(activity: Activity, connection: ClassifierServiceConncetion){
    activity.bindService(Intent(activity, ClassifiationService::class.java), connection, Context.BIND_AUTO_CREATE)
    startClassificationFragment(activity)
}
private fun startClassificationFragment(activity: Activity){
    activity.fragmentManager.beginTransaction()
            .add(R.id.fragment_container, ClassificationFragment())
            .commit()
}

