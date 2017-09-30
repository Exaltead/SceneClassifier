package com.exaltead.sceneclassifier.data_extraction

import android.media.AudioFormat.CHANNEL_IN_MONO
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.InvalidObjectException

const val SAMPLING_RATE = 20000 // not used, use the default currently
private const val TAG = "MicRecorder"
class MicrophoneRecorder : IAudioRecorder {
    private lateinit var audioRecorder: AudioRecord
    private val minBufferSize =  AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL_IN_MONO,
            ENCODING_PCM_16BIT)
    private var currentlyRecording = false
    init {
        Log.i("AudioRecorder", "min buffer size "+ minBufferSize.toString())

    }

    override fun start() {
        currentlyRecording = true
        // Use bigger buffer if needed
        audioRecorder = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE,
                CHANNEL_IN_MONO, ENCODING_PCM_16BIT, minBufferSize)
    }

    override fun stop() {
        currentlyRecording = false
        audioRecorder.stop()
    }


    override fun takeShortAudioRecord(duration: Double): ShortArray {
        if(! currentlyRecording){
            throw InvalidObjectException("Recording not active")
        }
        val samplingRate = audioRecorder.sampleRate
        val requiredArrayLength = (duration * samplingRate).toInt() + 1
        // Add 1 to force rounding to ceil
        val resultsArray = ShortArray(requiredArrayLength)
        audioRecorder.startRecording()
        when(audioRecorder.read(resultsArray,0, requiredArrayLength, AudioRecord.READ_BLOCKING)){
            AudioRecord.ERROR_BAD_VALUE -> Log.e(TAG, "BAD VALUE")
            AudioRecord.ERROR_DEAD_OBJECT -> Log.e(TAG, "DEAD OBJECT") //TODO: Object must be recreated
            AudioRecord.ERROR -> Log.e(TAG, "GENERAL ERROR")
        }
        audioRecorder.stop()
        return resultsArray
    }

}