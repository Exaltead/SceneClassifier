package com.exaltead.sceneclassifier.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.exaltead.sceneclassifier.R
import com.exaltead.sceneclassifier.classification.ClassificationResult
import kotlinx.android.synthetic.main.classification_view.view.*

class ClassificationAdapter(context: Context, resource: Int):
        ArrayAdapter<ClassificationResult>(context, resource, mutableListOf()){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = if (convertView == null){
            LayoutInflater.from(context)
                    .inflate(R.layout.classification_view, parent, false)
        }else{
            convertView
        }
        val probability = getItem(position)
        view.cls_label.text = probability.label
        view.cls_probability.text =  context.getString(R.string.double_precision)
                .format(probability.result)
        return view
    }
}

fun ClassificationAdapter.setContentAndNotify(data:List<ClassificationResult>?){
    if(data == null){
        return
    }
    this.clear()
    this.addAll(data)
    this.notifyDataSetChanged()
}
