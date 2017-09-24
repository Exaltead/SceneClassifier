package com.exaltead.sceneclassifier.data_extraction

class MfccFeatureExtractor(private val audioBufferer: IAudioBufferer, size: Int = 5) : IFeatureExtractor{
    override fun subscribeToExtractedFeatures(callback: (List<Double>) -> Unit) {
        TODO("not implemented")
    }

    private lateinit var observaion: (List<Double>) -> Unit
    private val buffer: MutableList<Double> = MutableList(size, {_ -> 0.0})

}