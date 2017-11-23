package com.exaltead.sceneclassifier.extraction

import be.tarsos.dsp.mfcc.MFCC


class MfccFeatureExtractor(private val audioRecorder: IAudioRecorder) : IFeatureExtractor{
    private val mfcc = MFCC(SAMPLE_MAX_LENGHT, SAMPLING_RATE)
    override fun receiveFeaturesForTimeSpan(): Array<Float> {
        val samples = audioRecorder.takeAudioRecord(SAMPLE_DURATION.toDouble())
        val bin = mfcc.magnitudeSpectrum(samples)
        val fbank = mfcc.melFilter(bin, mfcc.centerFrequencies)
        val f = mfcc.nonLinearTransformation(fbank)
        return mfcc.cepCoefficients(f).toTypedArray()

    }
}