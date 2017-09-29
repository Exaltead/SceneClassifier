package com.exaltead.sceneclassifier.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassifiationService
import com.exaltead.sceneclassifier.classification.ClassificationResult
import kotlinx.android.synthetic.main.classification_fragment.view.*


class ClassificationFragment: Fragment() {

    private lateinit var service: ClassifiationService
    private lateinit var adapter: ArrayAdapter<ClassificationResult>
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parentActivity = this.activity as MainActivity
        service = parentActivity.getClassificationService()!!
        val view =  inflater!!.inflate(R.layout.classification_fragment, container, false)
        adapter = ClassificationAdapter(this.context,
                R.layout.classification_view,service.classifications.value)
        view.classifications.adapter = adapter
        service.classifications.observe(this, Observer {t -> updateListing(t)} )
        return view
    }

    private fun updateListing(newListing: List<ClassificationResult>?){
        //TODO: remove this monstrosity
        adapter.clear()
        adapter.addAll(newListing)
        adapter.notifyDataSetChanged()
    }
}