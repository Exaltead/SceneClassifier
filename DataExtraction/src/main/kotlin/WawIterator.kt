import phonecs.readWawFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

const val resPrefix = "res/"
internal data class DatasetHolder(val path: String, val type: String, val location: String)

class WawIterable(metaFileLocation: String) : Iterable<DoubleArray> {

    internal val locations: List<DatasetHolder> = readMetadataFile(metaFileLocation)

    override fun iterator(): Iterator<DoubleArray> {
        return WawIterator(this)
    }

}

private class WawIterator(private val iterable: WawIterable) : Iterator<DoubleArray> {

    private var index = 0
    override fun hasNext(): Boolean {
        for(i in index until iterable.locations.size){
            if(File(resPrefix + iterable.locations[i].path).exists()){
                println("Found "+ iterable.locations[i].path)
                return true
            }
            else{
                println("Skipping " + iterable.locations[i].path)
                index++
                continue
            }
        }
        println("No more files in meta file")
        return false
    }

    override fun next(): DoubleArray {
        index++
        return readWawFile(resPrefix + iterable.locations[index-1].path)
    }

}

private fun String.parseDatasetHolder(): DatasetHolder {
    return this.trim().split('\t').toDatasetHolder()
}

private fun List<String>.toDatasetHolder(): DatasetHolder {
    return DatasetHolder(this[0], this[1], this[2])
}

private fun readMetadataFile(filename: String): List<DatasetHolder> {
    return BufferedReader(FileReader(filename)).useLines { t -> t.map { l -> l.parseDatasetHolder() }.toList() }
}