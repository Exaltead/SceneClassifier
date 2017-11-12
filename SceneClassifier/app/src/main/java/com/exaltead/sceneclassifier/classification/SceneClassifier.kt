package com.exaltead.sceneclassifier.classification

import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.ui.App
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import java.io.Closeable
import java.io.FileInputStream


private const val TAG = "SceneClassifier"
//private val MODEL_FILE_NAME = Environment.getExternalStorageDirectory().name+"/model.pb"
private const val OUTPUT_TENSOR_NAME = "tuutuut"
private const val INPUT_TENSOR_NAME = "dadaa"
private const val SAMPLE_LENGHT = 30L
private const val NUMBER_OF_CLASSES = 15
private const val MODEL_FILE = "file:///android_asset/optimized_tfdroid.pb";

class SceneClassifier(private val featureExtractor: IFeatureExtractor):Closeable {
    private val inference: TensorFlowInferenceInterface
    init {
        inference = TensorFlowInferenceInterface(App.context.assets, MODEL_FILE)
    }
    override fun close() {
        inference.close()
    }

    companion object {
        init {
            System.loadLibrary("tensorflow_inference")
        }
    }

    val labels = List(15, { i -> i.toString() })

    fun getCurrentClassification(): List<ClassificationResult>{
        val inputs = featureExtractor.receiveFeaturesForTimeSpan()
        inference.feed(INPUT_TENSOR_NAME, inputs.toFloatArray(), SAMPLE_LENGHT)
        inference.run(arrayOf(OUTPUT_TENSOR_NAME))
        val results = FloatArray(NUMBER_OF_CLASSES)
        inference.fetch(OUTPUT_TENSOR_NAME, results)

        return groupByTime(inputs)
                .mapIndexed { index, d -> ClassificationResult(index.toString(), d) } }
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