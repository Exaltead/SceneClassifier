import be.tarsos.dsp.mfcc.MFCC

fun main(args: Array<String>){
    val mfcc = MFCC(SAMPLE_MAX_LENGHT, SAMPLING_RATE)
    val wavFileIterable = WawIterable("res/train_meta.txt")
    for (i in wavFileIterable){
        print("Average" + i.average())
        print(" Min:" + i.min())
        print(" Max:" + i.max())
        print(" Size " + i.size +'\n')
        val segments = calculateMfccMfccSegements(i.map { t -> t.toFloat() }.toFloatArray(), mfcc)
    }
}