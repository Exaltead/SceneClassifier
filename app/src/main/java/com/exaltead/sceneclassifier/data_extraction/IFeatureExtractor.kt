package com.exaltead.sceneclassifier.data_extraction

interface IFeatureExtractor{
    fun subscribeToExtractedFeatures(callback: (List<Double>) -> Unit)
}