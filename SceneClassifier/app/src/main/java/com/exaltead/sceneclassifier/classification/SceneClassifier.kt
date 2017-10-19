package com.exaltead.sceneclassifier.classification

import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor

private const val TAG = "SceneClassifier"

class SceneClassifier(private val featureExtractor: IFeatureExtractor) {
    fun getCurrentClassification(): List<ClassificationResult> {
        val samples = featureExtractor.receiveFeaturesForTimeSpan()
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