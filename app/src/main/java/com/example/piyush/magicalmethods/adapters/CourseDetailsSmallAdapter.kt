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
import com.example.piyush.magicalmethods.activity.CourseDetailsActivity
import com.example.piyush.magicalmethods.R
import com.example.piyush.magicalmethods.entity.CourseDetails


class CourseDetailsSmallAdapter(private val context: Context, private val courseDetails: ArrayList<CourseDetails>) : RecyclerView.Adapter<CourseDetailsSmallAdapter.MyViewAdapter>() {


    override fun onBindViewHolder(holder: MyViewAdapter?, position: Int) {
        holder?.title?.text = courseDetails[position].name
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

    override fun getItemCount(): Int {
        return courseDetails.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewAdapter {
        val inflater = LayoutInflater.from(parent?.context)
        val v: View = inflater.inflate(R.layout.item_course_details_small, parent, false)


        // Set view's sizes, margins, paddings and layout parameters
        // Returns as a viewholder
        return this.MyViewAdapter(v)
    }

    inner class MyViewAdapter(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById(R.id.course_details_small_image)
        var title: TextView = v.findViewById(R.id.course_details_small_title)
        var layout: View = v
    }
}