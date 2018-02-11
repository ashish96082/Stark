package com.example.piyush.magicalmethods.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.piyush.magicalmethods.R
import com.example.piyush.magicalmethods.activity.CourseDetailsActivity
import com.example.piyush.magicalmethods.entity.CourseDetails
import com.example.piyush.magicalmethods.lib.Util

/**
 * Created by ashish kumar
 * on 11-02-2018 | 12:11 PM.
 */
class CourseDetailsAdapter(private val context: Context, private var courseDetails: ArrayList<CourseDetails>) : RecyclerView.Adapter<CourseDetailsAdapter.MyViewAdapter>() {
    override fun onBindViewHolder(holder: MyViewAdapter?, position: Int) {
        holder?.title?.text = courseDetails[position].name
        holder?.description?.text = courseDetails[position].description
        holder?.price?.text = Util.formatNumber(courseDetails[position].price)
        Glide
                .with(context)
                .load(courseDetails[position].image)
                .crossFade()
                .centerCrop()
                .into(holder?.image)

        holder?.layout?.setOnClickListener {
            context.startActivity(Intent(context, CourseDetailsActivity::class.java)
                    .putExtra("courseKey", courseDetails[position].courseKey))
        }
    }

    fun addData(courses: ArrayList<CourseDetails>) {
        val previousSize = courseDetails.size + 1
        for (course in courses)
            courseDetails.add(course)
        notifyDataSetChanged()
    }

    fun setFilter(newList: java.util.ArrayList<CourseDetails>) {
        courseDetails = java.util.ArrayList()
        courseDetails.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return courseDetails.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewAdapter {
        val inflater = LayoutInflater.from(parent?.context)
        val v: View = inflater.inflate(R.layout.item_course_card, parent, false)


        // Set view's sizes, margins, paddings and layout parameters
        // Returns as a viewholder
        return this.MyViewAdapter(v)
    }

    inner class MyViewAdapter(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById(R.id.course_card_image)
        var title: TextView = v.findViewById(R.id.course_card_title)
        val description: TextView = v.findViewById(R.id.course_card_description)
        val price: TextView = v.findViewById(R.id.course_card_price)
        var layout: View = v
    }
}