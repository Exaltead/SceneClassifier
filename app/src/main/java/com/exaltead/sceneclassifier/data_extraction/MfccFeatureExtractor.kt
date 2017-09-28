package com.exaltead.sceneclassifier.data_extraction

import android.util.Log

class MfccFeatureExtractor(private val audioBufferer: IAudioBufferer, size: Int = 5) : IFeatureExtractor{
    override fun receiveFeaturesForTimeSpan(time: Double): Array<Double> {
        val samples = audioBufferer.takeShortAudioRecord(time)
        return Array(0,{_-> 0.0})
    }

    override fun subscribeToExtractedFeatures(callback: (List<Double>) -> Unit) {
        TODO("not implemented")
    }

    private lateinit var observaion: (List<Double>) -> Unit
    private val buffer: MutableList<Double> = MutableList(size, {_ -> 0.0})

}