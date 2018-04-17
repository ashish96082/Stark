package com.magicalmethods.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

/**
 * Created by ashish kumar
 * on 15-03-2018 | 08:07 PM.
 */
class NoFilterArrayAdapter<T>(context: Context, textViewResourceId: Int, objects: List<T>) :
        ArrayAdapter<T>(context, textViewResourceId, objects) {
    private val filter = NoFilter()
    var items : List<T> = objects

    override fun getFilter(): Filter {
        return filter
    }

    private inner class NoFilter: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            filterResults.values = items
            filterResults.count = items.count()
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }

    }

}