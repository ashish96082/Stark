package com.example.piyush.magicalmethods.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.piyush.magicalmethods.R
import com.example.piyush.magicalmethods.activity.VideoPlayerActivity
import com.example.piyush.magicalmethods.entity.CourseVideo

/**
 * Created by ashish kumar
 * on 08-02-2018 | 02:32 AM.
 */

class CourseDetailsVideosAdapter(private val context: Context, private val videos: ArrayList<CourseVideo>) : RecyclerView.Adapter<CourseDetailsVideosAdapter.MyViewAdapter>() {


    override fun onBindViewHolder(holder: MyViewAdapter?, position: Int) {
        holder?.title?.text = videos[position].title
        holder?.description?.text = videos[position].description
        if (videos[position].free == 1) {
            holder?.playIcon?.setImageResource(R.drawable.play)
        }

        holder?.layout?.setOnClickListener {
            if (videos[position].free == 1)
                context.startActivity(Intent(context, VideoPlayerActivity::class.java)
                        .putExtra("videoKey", videos[position].videoKey)
                        .putExtra("vimeoVideoId", videos[position].vimeoId))
            else
                Toast.makeText(context, "Purchase the course to view this video", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewAdapter {
        val inflater = LayoutInflater.from(parent?.context)
        val v: View = inflater.inflate(R.layout.video_course_details_item, parent, false)


        // Set view's sizes, margins, paddings and layout parameters
        // Returns as a viewholder
        return this.MyViewAdapter(v)
    }

    inner class MyViewAdapter(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView = v.findViewById(R.id.course_details_video_title)
        var description: TextView = v.findViewById(R.id.course_details_video_description)
        var playIcon: ImageView = v.findViewById(R.id.course_details_video_play_icon)
        var layout: View = v
    }
}