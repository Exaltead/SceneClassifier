import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import javax.sound.sampled.AudioInputStream


private data class DatasetHolder(val path: String, val type: String, val location: String)

class WawIterable(metaFileLocation: String) : Iterable<FloatArray> {
    private val locations: List<DatasetHolder> = readMetadataFile(metaFileLocation)
    override fun iterator(): Iterator<FloatArray> {
        return WawIterator(this)
    }

    fun canReadNext(): Boolean {
        return false
    }

    fun getNext(): FloatArray {
        return FloatArray(0)
    }
}

private class WawIterator(private val iterable: WawIterable) : Iterator<FloatArray> {
    override fun hasNext(): Boolean {
        return iterable.canReadNext()
    }

    override fun next(): FloatArray {
        return iterable.getNext()
    }

}

private fun AudioInputStream.toFloatArray(): FloatArray {
    //TODO: implement
    TODO()
}

private fun String.parseDatasetHolder(): DatasetHolder {
    return this.trim().split('\t').toDatasetHolder()
}

private fun List<String>.toDatasetHolder(): DatasetHolder {
    return DatasetHolder(this[0], this[1], this[2])
}

private fun readMetadataFile(filename: String): List<DatasetHolder> {
    return BufferedReader(FileReader(filename)).useLines {  t -> t.map { l -> l.parseDatasetHolder() }.toList() }
}