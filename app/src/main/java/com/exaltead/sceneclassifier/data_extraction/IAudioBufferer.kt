package com.exaltead.sceneclassifier.data_extraction

interface IAudioBufferer {

    /// Starts feeding audio to the ringbuffer
    fun start()

    /// Stops the audio feeding
    fun stop()

    /// If the system is feeding data, try to call the callback at most every elapsed second.
    /// Time between each call might take longer than the minimum time.
    fun subscribeToElapsedTime(callback:()-> Unit, minimumTime: Double = 0.5)
}