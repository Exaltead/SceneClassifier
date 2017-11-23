package com.exaltead.sceneclassifier.classification

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.exaltead.sceneclassifier.extraction.IAudioRecorder
import com.exaltead.sceneclassifier.extraction.IFeatureExtractor
import com.exaltead.sceneclassifier.extraction.MfccFeatureExtractor
import com.exaltead.sceneclassifier.extraction.MicrophoneRecorder
import com.exaltead.sceneclassifier.ui.ClassificationViewModel
import java.util.*


const val AUDIO_SOUCE_TYPE = "AUDIO_SOURCE"

enum class AUDIO_SOURCE_TYPE{
    REAL_TIME,
    FILE,
    NOT_CHOSEN
}

data class ClassificationResult(val label: String, val result: Double)
class ClassifierBinder(val service: ClassifiationService): Binder()

private class UpdateTask(val service: ClassifiationService): TimerTask() {
    override fun run() {
        service.updateStatistics()
    }
}

private const val UPDATE_FREQUENCY = 1000L // 1s
class ClassifiationService : Service(){
    private var viewModel: ClassificationViewModel? = null
    private var inputType: AUDIO_SOURCE_TYPE = AUDIO_SOURCE_TYPE.NOT_CHOSEN
    private lateinit var timer: Timer
    private lateinit var classifier: SceneClassifier
    private lateinit var audioRecorder: IAudioRecorder


    override fun onBind(p0: Intent?): IBinder {
        Log.d("ClassificationService", "Service bound")
        return ClassifierBinder(this)
    }


    fun activateFromInput(source: AUDIO_SOURCE_TYPE, targetViewModel: ClassificationViewModel){
        inputType = source
        viewModel = targetViewModel
        when(source){
            AUDIO_SOURCE_TYPE.REAL_TIME -> allocateResourcesForRealtime()
            AUDIO_SOURCE_TYPE.FILE -> TODO()
            AUDIO_SOURCE_TYPE.NOT_CHOSEN -> TODO()
        }
        timer = Timer()
        timer.schedule(UpdateTask(this), 0, UPDATE_FREQUENCY)
    }

    fun releaseResources(){
        if(inputType == AUDIO_SOURCE_TYPE.NOT_CHOSEN){
            return
        }
        timer.cancel()
        if(inputType == AUDIO_SOURCE_TYPE.REAL_TIME){
            audioRecorder.release()
            classifier.close()
        }
        else{
            // TODO: Release file resources
        }
        viewModel = null
    }

    private fun allocateResourcesForRealtime(){

        audioRecorder = MicrophoneRecorder()
        val extractor: IFeatureExtractor = MfccFeatureExtractor(audioRecorder)
        classifier = SceneClassifier(extractor)
    }

    fun updateStatistics(){
        viewModel?.data?.postValue(classifier.getCurrentClassification())
    }
}

