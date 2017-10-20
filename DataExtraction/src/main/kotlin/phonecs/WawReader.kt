package phonecs

import java.io.File

fun readWawFile(filename: String): FloatArray{
    val wawFile = WavFile.openWavFile(File(filename))
    wawFile.display()
    return FloatArray(0)
}