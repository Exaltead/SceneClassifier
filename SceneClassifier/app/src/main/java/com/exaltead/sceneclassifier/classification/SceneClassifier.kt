package com.exaltead.sceneclassifier.classification

import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.ui.App
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import java.io.Closeable


private const val TAG = "SceneClassifier"
//private val MODEL_FILE_NAME = Environment.getExternalStorageDirectory().name+"/model.pb"
private const val OUTPUT_TENSOR_NAME = "tuutuut"
private const val INPUT_TENSOR_NAME = "dadaa"
private const val SAMPLE_LENGHT = 30L
private const val NUMBER_OF_CLASSES = 15
private const val MODEL_FILE = "fmodel.pb"
private const val LABEL_FILE = "labels.txt"

class SceneClassifier(private val featureExtractor: IFeatureExtractor):Closeable {
    private val labels: List<String>
    private val inference: TensorFlowInferenceInterface
    private var isClosed = false
    init {
        val manager = App.context.assets
        inference = TensorFlowInferenceInterface(manager, MODEL_FILE)

        labels = manager.open(LABEL_FILE).use { t -> readLabels(t.reader().readLines())}
    }
    override fun close() {
        synchronized(isClosed, {
            isClosed = true
            inference.close()})

    }

    companion object {
        init {
            System.loadLibrary("tensorflow_inference")
        }
    }


    fun getCurrentClassification(): List<ClassificationResult> =
            synchronized(isClosed, { classify()})

    private fun classify(): List<ClassificationResult> {
        if(isClosed){
            return emptyList()
        }
        val inputs = featureExtractor.receiveFeaturesForTimeSpan()
        inference.feed(INPUT_TENSOR_NAME, inputs.toFloatArray(), 1, SAMPLE_LENGHT)
        inference.run(arrayOf(OUTPUT_TENSOR_NAME))
        val results = FloatArray(NUMBER_OF_CLASSES)
        inference.fetch(OUTPUT_TENSOR_NAME, results)
        return results.mapIndexed({i, f -> ClassificationResult(labels[i], f.toDouble())})
    }
}



private fun readLabels(input: List<String>): List<String>{
    val output = MutableList(input.size, {_ -> ""})
    input.map { t -> t.split(',') }
            .map { a -> Pair(a[0].toInt(), a[1])}
            .map { b -> output[b.first] = b.second }
    return output.toList()
}
