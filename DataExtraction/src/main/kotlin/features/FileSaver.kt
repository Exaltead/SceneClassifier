package features


import java.io.FileWriter

fun saveFeatures(features: List<Feature>, filename: String){
    FileWriter(filename).use { t -> features.map { f -> t.write(f.toCsvRow())}}
}

private fun Feature.toCsvRow(): String{
    return "$location,$type${features.fold("", { s, f -> s + ',' + f.toString()})}\n"
}