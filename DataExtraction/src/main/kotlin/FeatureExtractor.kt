import be.tarsos.dsp.mfcc.MFCC

const val SAMPLING_RATE = 44100
const val SAMPLE_MAX_LENGHT = 1 * SAMPLING_RATE
class FeatureExtractor {

    private val mfcc = MFCC(SAMPLE_MAX_LENGHT, SAMPLING_RATE)

    fun splitToMfccSegements(samples: FloatArray): List<FloatArray> {
        TODO()
    }

    private fun calculateMfcc(samples: FloatArray): FloatArray {

        val bin = mfcc.magnitudeSpectrum(samples)
        val fbank = mfcc.melFilter(bin, mfcc.centerFrequencies)
        val f = mfcc.nonLinearTransformation(fbank)
        return mfcc.cepCoefficients(f)

    }
}