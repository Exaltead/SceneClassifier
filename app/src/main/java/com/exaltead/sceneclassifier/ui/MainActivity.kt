package com.exaltead.sceneclassifier.ui

import android.app.Activity
import android.os.Bundle
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ISceneClassifier
import com.exaltead.sceneclassifier.classification.TensorflowSceneClassifier
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private lateinit var adapter: ClassificationAdapter
    private lateinit var classifier: ISceneClassifier
    private lateinit var classificationProbabilities: Array<Double>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        classifier = TensorflowSceneClassifier()
        classificationProbabilities = classifier.getCurrentClassification().toTypedArray()
        adapter = ClassificationAdapter(this, R.layout.classification_view,
                classificationProbabilities)
        classifications.adapter = adapter
    }
}
