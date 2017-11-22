package com.exaltead.sceneclassifier.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.AUDIO_SOUCE_FILE
import com.exaltead.sceneclassifier.classification.AUDIO_SOURCE_REALTIME
import kotlinx.android.synthetic.main.selection_fragment.view.*


class SelectionFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.selection_fragment, container, false)
        view.test_button.setOnClickListener({_ -> testButtonClicked()})
        view.realtime_button.setOnClickListener({_ -> realtimeButtonClicked()})
        return view
    }

    private fun testButtonClicked(){
        val parent = activity as MainActivity
        parent.selectAudioSouce(AUDIO_SOUCE_FILE)
    }

    private fun realtimeButtonClicked(){
        val parent = activity as MainActivity
        parent.selectAudioSouce(AUDIO_SOURCE_REALTIME)
    }

}