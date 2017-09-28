package com.exaltead.sceneclassifier.classification

import android.util.Log
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor

class SceneClassifier(private val featureExtractor: IFeatureExtractor) {
    fun getCurrentClassification(): DoubleArray {
        val samples = featureExtractor.receiveFeaturesForTimeSpan(1.0)
        Log.i("SceneClassifer", "Pulled "+ samples.size.toString() + "features")
        return DoubleArray(10, {_ -> Math.random()})
    }

}