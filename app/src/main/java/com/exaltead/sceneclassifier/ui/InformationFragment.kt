package com.exaltead.sceneclassifier.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exaltead.sceneclassifier.R
import kotlinx.android.synthetic.main.information_fragment.view.*


class InformationFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.information_fragment, container, false)
        val parent = this.activity as MainActivity
        view.information.text = parent.infoText
        return view
    }
}