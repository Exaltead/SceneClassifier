fun main(args: Array<String>){
    val extractor = FeatureExtractor()
    val wavFileIterable = WawIterable("res/train_meta.txt")
    for (i in wavFileIterable){
        print("Average" + i.average())
        print(" Min:" + i.min())
        print(" Max:" + i.max())
        print(" Size " + i.size +'\n')
        val result =  i.map { t -> t.toFloat() }
    }
}