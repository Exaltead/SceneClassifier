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
private const val READ_STORAGE_PERMISSION = 400

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
        bindClassificationService()
        // SHOW ERROR DIALOG / FRAGMENT
    }

    override fun onStop() {
        super.onStop()
        unbindClassificationService()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == RECORD_AUDIO_CODE && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission granted for audio")
            displaySelection()
        }
        else if (requestCode == READ_STORAGE_PERMISSION && grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission granted for storage reading")
            displaySelection()
        }
        else{
            Log.w(TAG, "Permission not granted")
        }
    }

    private fun checkAndRequestPermission(permission:String, code: Int):Boolean{
        return if(ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission is not yet granted")

            // TODO: show rationale: https://developer.android.com/training/permissions/requesting.html
            ActivityCompat.requestPermissions(this, Array(1,
                    { _ -> permission}), code)
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
        if(audioSource == AUDIO_SOURCE_TYPE.REAL_TIME &&
                !checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_CODE)){
            return
        }
        else if(audioSource == AUDIO_SOURCE_TYPE.FILE &&
                !checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION)){
            return
        }
        displaySelection()

    }

    fun startClassification(){
        if(isBound){
            Log.i(TAG, "Allocating resources for classification")
            getClassificationService()?.activateFromInput(souceSelection, viewModel)
        }
        else{
            Log.w(TAG, "Started classification when service is not bound")
        }
    }

    fun endClassification(){
        if(isBound){
            Log.i(TAG, "Releasing resources of the classification")
            getClassificationService()?.releaseResources()
        }
        else{
            Log.w(TAG, "Tried to  release resources of unbound service")
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


