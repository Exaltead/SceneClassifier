package com.exaltead.sceneclassifier.data_extraction

interface IFeatureExtractor{
    //TODO: Remove
    fun subscribeToExtractedFeatures(callback: (List<Double>) -> Unit)

    fun receiveFeaturesForTimeSpan(time: Double): Array<Double>

}