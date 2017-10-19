package com.exaltead.sceneclassifier.data_extraction

interface IAudioRecorder {

    fun takeAudioRecord(duration: Double): FloatArray

    fun start()
    fun stop()
}