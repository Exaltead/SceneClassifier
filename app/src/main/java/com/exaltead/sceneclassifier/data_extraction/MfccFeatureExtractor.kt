package com.exaltead.sceneclassifier.data_extraction

import com.exaltead.sceneclassifier.jni.MfccJni


class MfccFeatureExtractor(private val audioRecorder: IAudioRecorder) : IFeatureExtractor{
    private val  adapter: MfccJni = MfccJni()
    override fun receiveFeaturesForTimeSpan(time: Double): Array<Float> {
        adapter.init(SAMPLING_RATE)
        val samples = audioRecorder.takeShortAudioRecord(time)
        return samples.map { t -> t.toFloat() }.toTypedArray()
    }
}