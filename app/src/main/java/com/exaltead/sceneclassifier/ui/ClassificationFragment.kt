package com.exaltead.sceneclassifier.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassifiationService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.classification_fragment.view.*


class ClassificationFragment: Fragment() {

    private lateinit var service: ClassifiationService

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parentActivity = this.activity as MainActivity
        service = parentActivity.getClassificationService()
        val view =  inflater!!.inflate(R.layout.classification_fragment, container, false)

        view.classifications.adapter = ClassificationAdapter(this.context,
                R.layout.classification_view,service.classifications.value)
        return view
    }

}