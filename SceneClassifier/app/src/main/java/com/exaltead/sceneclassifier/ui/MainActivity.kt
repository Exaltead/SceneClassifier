package com.exaltead.sceneclassifier.ui

import android.Manifest
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
import com.exaltead.sceneclassifier.classification.AUDIO_SOURCE_TYPE
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassifierServiceConncetion

private const val TAG = "MainActivity"
private const val RECORD_AUDIO_CODE = 300

class MainActivity : FragmentActivity() {

    private val connection: ClassifierServiceConncetion = ClassifierServiceConncetion(this)
    private lateinit var viewModel: ClassificationViewModel
    var infoText = ""
    private lateinit var souceSelection: AUDIO_SOURCE_TYPE
    private var isBound = false

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
        bindClassificationService()
        // SHOW ERROR DIALOG / FRAGMENT
    }

    override fun onStop() {
        super.onStop()
        getClassificationService()?.releaseResources()
        unbindClassificationService()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == RECORD_AUDIO_CODE && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission granted")
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

    private fun getClassificationService(): ClassifiationService? = connection.binder?.service

    fun selectAudioSource(audioSource: AUDIO_SOURCE_TYPE){
        souceSelection = audioSource
        if(isBound){
            getClassificationService()?.activateFromInput(souceSelection, viewModel)
            displaySelection()
        }
    }

    private fun bindClassificationService(){
        Toast.makeText(this, resources.getString(R.string.binding_service),
                Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ClassifiationService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    private fun unbindClassificationService(){
        if(isBound){
            unbindService(connection)
            isBound = false
        }
    }

    private fun displaySelection(){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ClassificationFragment())
                .addToBackStack(null)
                .commit()
    }
}


