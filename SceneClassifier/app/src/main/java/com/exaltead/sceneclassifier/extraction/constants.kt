package com.exaltead.sceneclassifier.extraction

import android.media.AudioFormat
import android.media.AudioFormat.CHANNEL_IN_MONO

const val SAMPLING_RATE = 44100
const val SAMPLE_DURATION = 1
const val SAMPLE_MAX_LENGHT = SAMPLING_RATE * SAMPLE_DURATION
const val CHANNELS = CHANNEL_IN_MONO
const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_FLOAT
