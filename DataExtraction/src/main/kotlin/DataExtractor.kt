import be.tarsos.dsp.mfcc.MFCC
import features.Feature
import features.WawIterable
import features.makeToMfccFeatures

fun main(args: Array<String>){
    val mfcc = MFCC(SAMPLE_MAX_LENGHT, SAMPLING_RATE)
    val wavFileIterable = WawIterable("res/train_meta.txt")
    val features = readFeatures(wavFileIterable, mfcc)
}

fun readFeatures(wawIterable: WawIterable, mfcc: MFCC): List<Feature>{
    return wawIterable.flatMap { t -> t.makeToMfccFeatures(mfcc) }
}