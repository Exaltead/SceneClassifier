package com.exaltead.sceneclassifier.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassifierServiceConncetion
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private val connection: ClassifierServiceConncetion = ClassifierServiceConncetion()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, ClassifiationService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if(connection.binder != null){
            unbindService(connection)
        }
}
}
