package com.exaltead.sceneclassifier.data_extraction

interface IFeatureExtractor{
    fun receiveFeaturesForTimeSpan(time: Double): Array<Float>
}