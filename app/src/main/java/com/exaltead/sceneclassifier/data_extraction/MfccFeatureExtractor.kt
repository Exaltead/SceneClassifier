package com.exaltead.sceneclassifier.data_extraction

class MfccFeatureExtractor(private val audioRecorder: IAudioRecorder) : IFeatureExtractor{
    override fun receiveFeaturesForTimeSpan(time: Double): Array<Float> {
        val samples = audioRecorder.takeShortAudioRecord(time)
        return samples.map { t -> t.toFloat() }.toTypedArray()
    }
}