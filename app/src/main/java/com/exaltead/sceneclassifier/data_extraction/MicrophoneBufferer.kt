package com.exaltead.sceneclassifier.data_extraction

import android.media.AudioFormat.*
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log

const val samplingRate = 20000 // not used, use the default currently

class MicrophoneBufferer : IAudioBufferer {

    private val audioRecorder: AudioRecord
    init {
        val minBufferSize = AudioRecord.getMinBufferSize(samplingRate, CHANNEL_IN_MONO,
                ENCODING_PCM_FLOAT)
        Log.i("AudioRecorder", "min buffer size "+ minBufferSize.toString())
        // Use bigger buffer if needed
        audioRecorder = AudioRecord(MediaRecorder.AudioSource.MIC, samplingRate,
                CHANNEL_IN_MONO, ENCODING_PCM_8BIT, minBufferSize)
    }
    override fun takeShortAudioRecord(duration: Double): ShortArray {
        //TODO: Implement
        val samplinRate = audioRecorder.sampleRate
        val requiredArrayLength = duration * samplinRate
        return kotlin.ShortArray(requiredArrayLength.toInt() + 1)
    }

}