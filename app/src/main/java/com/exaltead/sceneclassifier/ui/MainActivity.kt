package com.exaltead.sceneclassifier.ui

import android.app.Activity
import android.os.Bundle
import com.exaltead.sceneclassifier.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private lateinit var classificationProbabilities: Array<Double>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
