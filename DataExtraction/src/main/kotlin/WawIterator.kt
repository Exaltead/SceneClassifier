import javax.sound.sampled.AudioInputStream


private data class DatasetHolder(val path: String, val type: String, val location: String)

class WawIterable: Iterable<FloatArray> {
    private val locations: MutableList<DatasetHolder> = mutableListOf()

    override fun iterator(): Iterator<FloatArray> {
        return WawIterator(this)
    }

    init {
        //TODO: Implement reading of the wav files
    }

    fun canReadNext(): Boolean{
        return false
    }

    fun getNext(): FloatArray{
        return FloatArray(0)
    }
}

private class WawIterator(private val iterable: WawIterable): Iterator<FloatArray>{
    override fun hasNext(): Boolean {
        return iterable.canReadNext()
    }

    override fun next(): FloatArray {
        return iterable.getNext()
    }

}

private fun AudioInputStream.toFloatArray():FloatArray{
    //TODO: implement
    TODO()
}