package com.exaltead.sceneclassifier.data_extraction

import java.io.File
import java.io.FileInputStream

// We use static filename
const val FILENAME = "/mnt/sd_card/monofied.wav"

class WawRecorder : IAudioRecorder {
    private val samples = readMonoWavData(FILENAME)
    private var current = 0
    private var samplingRate = 44100
    init {


    }
    override fun start() {
    }

    override fun stop() {
    }

    override fun takeAudioRecord(duration: Double): FloatArray {
        val reqSamples =(duration * samplingRate).toInt()
        if(current + reqSamples > samples.size){
            // We flip around
            val end = current + reqSamples - samples.size
            val result = FloatArray(reqSamples, {i -> samples[getRotatedIndex(current + i, samples.size)]})
            current = end
            return  result
        }
        current += reqSamples
        return FloatArray(reqSamples, {i -> samples[current + i]})
    }
}

private fun readMonoWavData(filename: String): FloatArray =
        FileInputStream(File(filename)).readBytes().pcm24Encode()

private fun ByteArray.pcm24Encode(): FloatArray{
    val result = FloatArray(this.size/3)
    // Skip the first 44 bits sine they are encoded
    val start = 44
    for (c in start..this.size/3 step 3){
        result[c - start] = combineBytes(this[c].toInt(), this[c+1].toInt(), this[c+2].toInt()).toFloat()
    }
    return result

}

private fun combineBytes(b1: Int, b2: Int, b3: Int): Int =
        b3 and 0xFF or (b2 and 0xFF shl 8) or (b1 and 0x0F shl 16)

private fun getRotatedIndex(index: Int, size: Int): Int{
    if(index >= size){
        return index - size
    }
    return index
}