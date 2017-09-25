package com.exaltead.sceneclassifier.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.exaltead.sceneclassifier.R
import kotlinx.android.synthetic.main.classification_view.view.*

class ClassificationAdapter(context: Context, resource: Int, items: Array<Double>):
        ArrayAdapter<Double>(context, resource, items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = if (convertView == null){
            LayoutInflater.from(context)
                    .inflate(R.layout.classification_view, parent, false)
        }else{
            convertView
        }
        val probability = getItem(position)
        view.cls_probability.text = probability.toString()
        return view
    }
}
