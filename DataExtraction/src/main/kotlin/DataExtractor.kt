import be.tarsos.dsp.mfcc.MFCC
import features.Feature
import features.WawIterable
import features.makeToMfccFeatures
import features.saveFeatures

fun main(args: Array<String>) {
    val meta = "../Resources/meta.txt"
    val trainResult = "../Resources/mfcc.csv"
    saveFeatures(readFeatures(WawIterable(meta), MFCC(SAMPLE_MAX_LENGHT, SAMPLING_RATE)), trainResult)

}

fun readFeatures(wawIterable: WawIterable, mfcc: MFCC): List<Feature> {
    return wawIterable.flatMap { t -> t.makeToMfccFeatures(mfcc) }
}