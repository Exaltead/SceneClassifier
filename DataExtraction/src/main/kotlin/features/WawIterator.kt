package features

import phonecs.readWawFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

const val resPrefix = "../Resources/"
internal data class DatasetHolder(val path: String, val type: String, val location: String)


class WawIterable(metaFileLocation: String) : Iterable<Feature> {

    internal val locations: List<DatasetHolder> = readMetadataFile(metaFileLocation)

    override fun iterator(): Iterator<Feature> {
        return WawIterator(this)
    }

}

private class WawIterator(private val iterable: WawIterable) : Iterator<Feature> {

    private var index = 0
    private var found = 0
    override fun hasNext(): Boolean {
        for(i in index until iterable.locations.size){
            if(File(resPrefix + iterable.locations[i].path).exists()){
                //println("Found: "+ iterable.locations[i].path + "remaining "+ (iterable.locations.size - i))
                found++
                if(found % 50 == 0){
                    println("Remaining ${iterable.locations.size - i} / ${iterable.locations.size}" )
                }
                return true
            }
            else{
                println("Skipping " + iterable.locations[i].path)
                index++
                continue
            }
        }
        println("No more files in meta file, read successfully $found of ${iterable.locations.size} files")
        return false
    }

    override fun next(): Feature {
        index++
        val datasetHolder = iterable.locations[index - 1]
        return Feature(datasetHolder.location,datasetHolder.type, readWawFile(resPrefix + datasetHolder.path)
                .map { t -> t.toFloat() }.toFloatArray())
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