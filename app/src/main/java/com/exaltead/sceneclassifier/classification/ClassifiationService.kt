package com.exaltead.sceneclassifier.classification

import android.app.Service
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.exaltead.sceneclassifier.data_extraction.IAudioBufferer
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MfccFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MicrophoneBufferer

data class ClassificationResult(val label: String, val result: Double)
class ClassifierBinder(val service: ClassifiationService): Binder()


class ClassifiationService : Service(){
    lateinit var classifications: MutableLiveData<List<ClassificationResult>>
    private lateinit var classifier: SceneClassifier
    private val localBinder  = ClassifierBinder(this)

    override fun onBind(p0: Intent?): IBinder {
        Log.d("ClassificationService", "Service bound")
        return localBinder
    }

    override fun onCreate() {
        super.onCreate()
        val audioBufferer: IAudioBufferer = MicrophoneBufferer()
        val extractor: IFeatureExtractor = MfccFeatureExtractor(audioBufferer)
        classifier = SceneClassifier(extractor)
        classifications.value = List<ClassificationResult>(5,
                {index -> ClassificationResult(index.toString(), Math.random()) })
    }
}

