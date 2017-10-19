package com.exaltead.sceneclassifier.data_extraction
interface IFeatureExtractor{
    fun receiveFeaturesForTimeSpan(): Array<Float>
}