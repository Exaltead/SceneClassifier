package com.exaltead.sceneclassifier.classification

import android.os.Environment
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.nio.file.Files
import java.nio.file.Path

private const val TAG = "SceneClassifier"
private const val MODEL_FILE_NAME = "/model.pb"

class SceneClassifier(private val featureExtractor: IFeatureExtractor) {
    val labels = List(15, {i -> i.toString()})
    private val sess: Session
    init {
        // Read the target file

        val protoBytes = readBytesFromProtobuf()
        val graph = Graph()
        graph.importGraphDef(protoBytes)

        sess = Session(graph)
        // init a classifier for it
        // do magic
    }

    fun getCurrentClassification(): List<ClassificationResult> {
        val samples = featureExtractor.receiveFeaturesForTimeSpan()
        // help from https://github.com/tensorflow/tensorflow/blob/master/tensorflow/java/src/main/java/org/tensorflow/examples/LabelImage.java
        //val result: Tensor<Float> = sess.runner().fetch("")

        //"Classification" results
        return groupByTime(samples).mapIndexed({ i, d -> ClassificationResult(i.toString(), d)})
    }

}

private fun groupByTime(samples:Array<Float>): List<Double>{
    val result: MutableList<Double> = mutableListOf()
    val accumulator: MutableList<Float> = mutableListOf()
    for ( i in samples.indices){
        if(i % samples.size / 10 == 0 && i != 0){
            result.add(accumulator.average())
            accumulator.clear()
        }
        accumulator.add(samples[i])
    }
    result.add(accumulator.average())
    return result
}

private fun readBytesFromProtobuf(filename: String): ByteArray{

}