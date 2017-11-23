package com.exaltead.sceneclassifier.extraction
interface IFeatureExtractor{
    fun receiveFeaturesForTimeSpan(): Array<Float>
}