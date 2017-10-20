//Heavily inspired by http://www.labbookpages.co.uk/audio/javaWavFiles.html
package phonecs

import java.io.File

fun readWawFile(filename: String): DoubleArray{
    println("Reading " + filename)
    val wawFile = WavFile.openWavFile(File(filename))
    wawFile.display()
    val  numberOfChannels = wawFile.numChannels
    val numberOfFrames = wawFile.numFrames
    // Allocate space for all of the frames
    val frames = DoubleArray(numberOfChannels * numberOfFrames.toInt())
    wawFile.readFrames(frames, wawFile.numFrames.toInt())
    wawFile.close()
    return frames
}