package com.exaltead.sceneclassifier.data_extraction

interface IAudioRecorder {

    fun takeShortAudioRecord(duration: Double): ShortArray

    fun start()
    fun stop()
}