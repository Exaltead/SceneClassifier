package com.exaltead.sceneclassifier.classification

import android.os.Environment
import android.util.Log
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import java.io.FileInputStream
import java.nio.FloatBuffer


private const val TAG = "SceneClassifier"
private val MODEL_FILE_NAME = Environment.getExternalStorageDirectory().name+"/model.pb"
private const val OUTPUT_TENSOR_NAME = "tuutuut"
private const val INPUT_TENSOR_NAME = "dadaa"
private const val SAMPLE_LENGHT = 30L
private const val NUMBER_OF_CLASSES = 15

class SceneClassifier(private val featureExtractor: IFeatureExtractor) {
    val labels = List(15, {i -> i.toString()})
    private val graph = Graph()
    init {
        // Read the target file

        val protoBytes = readBytesFromProtobuf(MODEL_FILE_NAME)
        graph.importGraphDef(protoBytes)
        Log.d(TAG, "Loaded model")


        // init a classifier for it
        // do magic
    }

    fun getCurrentClassification(): List<ClassificationResult> {
        val samples = featureExtractor.receiveFeaturesForTimeSpan()
        // help from https://github.com/tensorflow/tensorflow/blob/master
        // /tensorflow/java/src/main/java/org/tensorflow/examples/LabelImage.java
        val res = Session(graph).use { classify(it, samples.toFloatArray()) }
        //"Classification" results
        //return groupByTime(samples).mapIndexed({ i, d -> ClassificationResult(i.toString(), d)})
        return res.mapIndexed({ i, d -> ClassificationResult(i.toString(), d.toDouble())});
    }

}
private fun classify(sess: Session, samples: FloatArray): FloatArray{
    val buffer = FloatBuffer.wrap(FloatArray(NUMBER_OF_CLASSES))
    Tensor.create(longArrayOf(SAMPLE_LENGHT), FloatBuffer.wrap(samples))
            .use { o -> runTensors(sess, o, buffer)}
    return buffer.array()
}

private fun runTensors(sess: Session, inputTensor: Tensor<Float>, buffer: FloatBuffer) {
    sess.runner().feed(INPUT_TENSOR_NAME, inputTensor)
            .fetch(OUTPUT_TENSOR_NAME).run()
            .map { t -> t.use { it.writeTo(buffer) } }

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
    return FileInputStream(filename).use {
        it.readBytes()
    }
}