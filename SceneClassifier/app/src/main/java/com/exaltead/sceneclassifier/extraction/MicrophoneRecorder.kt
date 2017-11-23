package com.exaltead.sceneclassifier.extraction

import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.InvalidObjectException


private const val TAG = "MicRecorder"
class MicrophoneRecorder : IAudioRecorder {
    private var audioRecorder: AudioRecord? = null
    private val minBufferSize =  AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNELS,
            AUDIO_ENCODING)
    private var released = false
    init {
        // Create buffer size for at least 1 second
        Log.i(TAG, "min buffer size "+ minBufferSize.toString())
        audioRecorder = if(minBufferSize < SAMPLING_RATE * 1){
            AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE,
                    CHANNELS, AUDIO_ENCODING, SAMPLING_RATE * 1)
        }
        else{
            AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE,
                    CHANNELS, AUDIO_ENCODING, minBufferSize)
        }

        Log.i(TAG, "AudioRecord started")
    }


    override fun release() {
        synchronized(released, {releaseResources()})

    }


    override fun takeAudioRecord(duration: Double): FloatArray =
            synchronized(released, {syncronizeAudioCapture(duration)})

    private fun releaseResources(){
        if(released){
            return
        }
        audioRecorder?.release()
        audioRecorder = null
        released = true
    }

    private fun syncronizeAudioCapture(duration: Double): FloatArray{
        if(released){
            throw InvalidObjectException("Recording released")
        }
        val recorder = audioRecorder?: throw InvalidObjectException("Audio recorder not set")
        return captureAudio(recorder, duration)
    }

    private fun captureAudio(audioRecorder: AudioRecord, duration: Double): FloatArray{
        val samplingRate = audioRecorder.sampleRate
        val requiredArrayLength = (duration * samplingRate).toInt()
        val resultsArray = FloatArray(requiredArrayLength)
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


