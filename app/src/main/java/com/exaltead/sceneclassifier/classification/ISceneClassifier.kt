package com.exaltead.sceneclassifier.classification

interface ISceneClassifier {
    fun getCurrentClassification(): DoubleArray
}