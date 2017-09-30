package com.exaltead.sceneclassifier.classification

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.exaltead.sceneclassifier.data_extraction.IAudioBufferer
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MfccFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MicrophoneBufferer
import com.exaltead.sceneclassifier.ui.ClassificationViewModel
import java.util.*

data class ClassificationResult(val label: String, val result: Double)
class ClassifierBinder(val service: ClassifiationService): Binder()

private class UpdateTask(val service: ClassifiationService): TimerTask() {
    override fun run() {
        service.updateStatistics()
    }
}

private const val UPDATE_FREQUENCY = 2000L // 2s
class ClassifiationService : Service(){

    var viewModel: ClassificationViewModel? = null

    private var timer: Timer = Timer()
    private lateinit var classifier: SceneClassifier
    //private val localBinder  = ClassifierBinder(this)
    override fun onBind(p0: Intent?): IBinder {
        Log.d("ClassificationService", "Service bound")
        timer.scheduleAtFixedRate(UpdateTask(this), 0, UPDATE_FREQUENCY)
        return ClassifierBinder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        timer.purge()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        val audioBufferer: IAudioBufferer = MicrophoneBufferer()
        val extractor: IFeatureExtractor = MfccFeatureExtractor(audioBufferer)
        classifier = SceneClassifier(extractor)
    }

    fun updateStatistics(){
        viewModel?.data?.postValue(classifier.getCurrentClassification())
    }
}

