package com.exaltead.sceneclassifier.classification

interface ISceneClassifier {
    fun subscribeToClassification(callback: (probabilities: List<Float>) -> Unit)
}