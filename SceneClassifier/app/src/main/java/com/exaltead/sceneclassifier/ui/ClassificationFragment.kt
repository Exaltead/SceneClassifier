package com.exaltead.sceneclassifier.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassificationResult
import kotlinx.android.synthetic.main.classification_fragment.view.*


class ClassificationFragment: Fragment() {

    private lateinit var adapter: ClassificationAdapter
    private lateinit var viewModel: ClassificationViewModel


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProviders.of(activity).get(ClassificationViewModel::class.java)
        val view =  inflater!!.inflate(R.layout.classification_fragment, container, false)
        adapter = ClassificationAdapter(this.context, R.layout.classification_view)
        view.classifications.adapter = adapter
        viewModel.data.observe(this, Observer {t -> updateListing(t)} )
        startListUpdate()
        return view
    }

    override fun onPause() {
        super.onPause()
        endListUpdate()
    }

    private fun updateListing(newListing: List<ClassificationResult>?){
        adapter.setContentAndNotify(newListing)
    }

    private fun startListUpdate(){
        val parent = activity as MainActivity
        parent.startClassification()
    }

    private fun endListUpdate(){
        val parent = activity as MainActivity
        parent.endClassification()
    }
}