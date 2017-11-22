package com.exaltead.sceneclassifier.ui

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.AUDIO_SOUCE_TYPE
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassifierServiceConncetion

private const val TAG = "MainActivity"
private const val RECORD_AUDIO_CODE = 300

class MainActivity : FragmentActivity() {

    private val connection: ClassifierServiceConncetion = ClassifierServiceConncetion(this)
    private lateinit var viewModel: ClassificationViewModel
    var infoText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SelectionFragment())
                .commit()
        viewModel = ViewModelProviders.of(this).get(ClassificationViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        checkAndRequestPermission()
        //attemptBindClassificationService(this, connection)
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
            //attemptBindClassificationService(this, connection)
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

    private fun getClassificationService(): ClassifiationService?{
        return connection.binder?.service
    }

    fun notifyServiceReady(){
        getClassificationService()?.viewModel = viewModel
        displaySelection()
    }

    fun selectAudioSouce(audioSource: Int){
        attemptBindClassificationService(this, connection, audioSource)
    }

    private fun attemptBindClassificationService(activity: Activity,
                                                 connection: ClassifierServiceConncetion,
                                                 audioSouceType: Int){
        infoText = resources.getString(R.string.binding_service)
        Toast.makeText(this, infoText, Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, ClassifiationService::class.java)
        intent.putExtra(AUDIO_SOUCE_TYPE, audioSouceType)
        when (activity.bindService(intent, connection, Context.BIND_AUTO_CREATE)){
            true -> Log.i(TAG, "Service successfully bound")
            false -> Log.i(TAG, "Binding service failed")
        }
    }

    private fun displaySelection(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ClassificationFragment())
                .addToBackStack(null)
                .commit()
    }
}

private fun startInfoFragment(activity: FragmentActivity){
    activity.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, InformationFragment())
            .commit()
}

