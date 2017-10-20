fun main(args: Array<String>){
    val wavFileIterable = WawIterable("res/train_meta.txt")
    for (i in wavFileIterable){
        print("Average" + i.average())
        print("Min:" + i.min())
        print("Max:" + i.max())
        print("size" + i.size +'\n')
    }
}