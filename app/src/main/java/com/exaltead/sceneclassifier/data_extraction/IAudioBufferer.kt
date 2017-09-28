package com.exaltead.sceneclassifier.data_extraction

interface IAudioBufferer {

    fun takeShortAudioRecord(duration: Double): ShortArray
}