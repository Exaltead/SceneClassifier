package com.exaltead.sceneclassifier.data_extraction

class MfccFeatureExtractor(private val audioBufferer: IAudioBufferer) : IFeatureExtractor{
    override fun receiveFeaturesForTimeSpan(time: Double): Array<Float> {
        val samples = audioBufferer.takeShortAudioRecord(time)
        return samples.map { t -> t.toFloat() }.toTypedArray()
    }
}