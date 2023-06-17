package com.example.yandextodo.view.detail

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val item = getItem(position)

        if (item == "!!Высокий") {
            val highlightColor = resolveColorAttr(context, com.google.android.material.R.attr.colorOnError)
            textView.setTextColor(highlightColor)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val item = getItem(position)

        if (item == "!!Высокий") {
            val highlightColor = resolveColorAttr(context, com.google.android.material.R.attr.colorOnError)
            textView.setTextColor(highlightColor)        }

        return view
    }

    private fun resolveColorAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
