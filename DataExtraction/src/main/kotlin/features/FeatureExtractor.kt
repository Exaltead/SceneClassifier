package features

import SAMPLE_MAX_LENGHT
import be.tarsos.dsp.mfcc.MFCC


fun Feature.makeToMfccFeatures(mfcc: MFCC): List<Feature>{
    return calculateMfccSegements(features, mfcc).map { t -> Feature(location, type, t) }
}

fun makeToMfccFeatures(location: String, type: String, samples: FloatArray, mfcc: MFCC): List<Feature> {
    return calculateMfccSegements(samples, mfcc).map { t -> Feature(location, type, t) }
}

fun calculateMfccSegements(samples: FloatArray, mfcc: MFCC): List<FloatArray> {
    return splitToSegments(samples).map { t -> t.getMfcc(mfcc) }

}

private fun splitToSegments(mainArray: FloatArray): List<FloatArray> {
    return (0 .. mainArray.size - SAMPLE_MAX_LENGHT step SAMPLE_MAX_LENGHT)
            .map { FloatArray(SAMPLE_MAX_LENGHT, { index -> mainArray[it + index]}) }
}


private fun FloatArray.getMfcc(mfcc: MFCC): FloatArray {
    val bin = mfcc.magnitudeSpectrum(this)
    val fbank = mfcc.melFilter(bin, mfcc.centerFrequencies)
    val f = mfcc.nonLinearTransformation(fbank)
    return mfcc.cepCoefficients(f)

}