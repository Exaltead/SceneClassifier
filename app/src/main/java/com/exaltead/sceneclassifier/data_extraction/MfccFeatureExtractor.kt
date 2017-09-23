package com.exaltead.sceneclassifier.data_extraction

class MfccFeatureExtractor(private val audioBufferer: IAudioBufferer, size: Int = 5) : IFeatureExtractor{
    private lateinit var observaion: (List<Double>) -> Unit
    private val buffer: MutableList<Double> = MutableList(size, {_ -> 0.0})
    init {
        audioBufferer.subscribeToElapsedTime({t -> extractFeaturesFromBlock(t)})
    }
    override fun subscribeToExtractedFeatures(callback: (List<Double>) -> Unit) {
        //TODO: trigger recording
        observaion = callback
    }

    private fun extractFeaturesFromBlock(samples: List<Double>){
        // TODO: calcualte Mfcc from the samples
        observaion(buffer)
    }
}