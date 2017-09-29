package com.exaltead.sceneclassifier.data_extraction

class MfccFeatureExtractor(private val audioBufferer: IAudioBufferer, size: Int = 5) : IFeatureExtractor{
    override fun receiveFeaturesForTimeSpan(time: Double): Array<Float> {
        val samples = audioBufferer.takeShortAudioRecord(time)
        samples.sort()
        return samples.map { t -> t.toFloat() }.toTypedArray()
    }
}