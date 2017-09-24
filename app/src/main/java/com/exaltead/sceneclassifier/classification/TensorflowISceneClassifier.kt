package com.exaltead.sceneclassifier.classification

class TensorflowSceneClassifier : ISceneClassifier {
    override fun getCurrentClassification(): DoubleArray {
        return DoubleArray(10, {_ -> Math.random()})
    }

}