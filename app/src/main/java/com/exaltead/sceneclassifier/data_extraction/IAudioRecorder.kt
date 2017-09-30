package com.exaltead.sceneclassifier.data_extraction

interface IAudioRecorder {

    fun takeShortAudioRecord(duration: Double): ShortArray
}