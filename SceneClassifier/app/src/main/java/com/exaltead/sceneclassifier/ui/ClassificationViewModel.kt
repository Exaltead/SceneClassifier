package com.exaltead.sceneclassifier.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.exaltead.sceneclassifier.classification.ClassificationResult


class ClassificationViewModel: ViewModel() {
    val data: MutableLiveData<List<ClassificationResult>> = MutableLiveData()
}