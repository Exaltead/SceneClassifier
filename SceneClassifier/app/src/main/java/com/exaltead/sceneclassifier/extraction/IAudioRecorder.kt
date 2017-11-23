package com.exaltead.sceneclassifier.extraction

interface IAudioRecorder {

    fun takeAudioRecord(duration: Double): FloatArray

    fun release()
}