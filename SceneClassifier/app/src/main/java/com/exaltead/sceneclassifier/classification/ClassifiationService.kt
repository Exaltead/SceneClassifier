package com.exaltead.sceneclassifier.classification

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.exaltead.sceneclassifier.data_extraction.IAudioRecorder
import com.exaltead.sceneclassifier.data_extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MfccFeatureExtractor
import com.exaltead.sceneclassifier.data_extraction.MicrophoneRecorder
import com.exaltead.sceneclassifier.ui.ClassificationViewModel
import java.util.*

data class ClassificationResult(val label: String, val result: Double)
class ClassifierBinder(val service: ClassifiationService): Binder()

private class UpdateTask(val service: ClassifiationService): TimerTask() {
    override fun run() {
        service.updateStatistics()
    }
}

private const val UPDATE_FREQUENCY = 1000L // 1s
class ClassifiationService : Service(){
    var viewModel: ClassificationViewModel? = null
    private lateinit var timer: Timer
    private lateinit var classifier: SceneClassifier
    private lateinit var audioRecorder: IAudioRecorder


    override fun onBind(p0: Intent?): IBinder {
        Log.d("ClassificationService", "Service bound")
        audioRecorder.start()
        timer = Timer()
        timer.schedule(UpdateTask(this), 0, UPDATE_FREQUENCY)
        return ClassifierBinder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        timer.cancel()
        audioRecorder.stop()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        audioRecorder = MicrophoneRecorder()
        val extractor: IFeatureExtractor = MfccFeatureExtractor(audioRecorder)
        classifier = SceneClassifier(extractor)
    }

    fun updateStatistics(){
        viewModel?.data?.postValue(classifier.getCurrentClassification())
    }
}
