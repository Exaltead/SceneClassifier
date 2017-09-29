package com.exaltead.sceneclassifier.data_extraction

import android.media.AudioFormat.CHANNEL_IN_MONO
import android.media.AudioFormat.ENCODING_PCM_FLOAT
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log

const val samplingRate = 20000 // not used, use the default currently
private const val TAG = "MicRecorder"
class MicrophoneBufferer : IAudioBufferer {

    private val audioRecorder: AudioRecord
    private val minBufferSize =  AudioRecord.getMinBufferSize(samplingRate, CHANNEL_IN_MONO,
            ENCODING_PCM_FLOAT)
    init {
        Log.i("AudioRecorder", "min buffer size "+ minBufferSize.toString())
        // Use bigger buffer if needed
        audioRecorder = AudioRecord(MediaRecorder.AudioSource.MIC, samplingRate,
                CHANNEL_IN_MONO, ENCODING_PCM_FLOAT, minBufferSize)
    }
    override fun takeShortAudioRecord(duration: Double): FloatArray {
        //TODO: Implement
        val samplinRate = audioRecorder.sampleRate
        val requiredArrayLength = (duration * samplinRate).toInt() + 1
        // Add 1 to force rounding to ceil
        val resultsArray = FloatArray(requiredArrayLength)
        when(audioRecorder.read(resultsArray,0, requiredArrayLength, AudioRecord.READ_BLOCKING)){
            AudioRecord.SUCCESS ->  return resultsArray
            AudioRecord.ERROR_BAD_VALUE -> Log.e(TAG, "BAD VALUE")
            AudioRecord.ERROR_DEAD_OBJECT -> Log.e(TAG, "DEAD OBJECT") //TODO: Object must be recreated
            AudioRecord.ERROR -> Log.e(TAG, "GENERAL ERROR")
        }
        return FloatArray(0)
    }

}