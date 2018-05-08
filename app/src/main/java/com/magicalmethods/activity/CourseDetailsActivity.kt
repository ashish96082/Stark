package com.magicalmethods.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.magicalmethods.R
import com.magicalmethods.adapters.CourseDetailsVideosAdapter
import com.magicalmethods.entity.CourseVideo
import com.magicalmethods.lib.MMfromServer
import com.magicalmethods.lib.Util
import com.magicalmethods.listeners.MMfromServerListener
import kotlinx.android.synthetic.main.content_course_details.*


class CourseDetailsActivity : AppCompatActivity() {
    private var courseKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)
//        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        courseKey = intent.getStringExtra("courseKey")
        if (courseKey == null)
            onBackPressed()

        course_details_videos_recyclerview.layoutManager = LinearLayoutManager(this)
        course_details_videos_recyclerview.hasFixedSize()
        course_details_videos_recyclerview.isNestedScrollingEnabled = false

        course_details_toggle_description.setOnClickListener { toggleVideoDetailsOpenIconRotation() }

        course_details_action_button.setOnClickListener {
            startActivity(Intent(this, PaymentWebviewActivity::class.java).putExtra("courseKey", courseKey))
        }

        loadingData()
    }

    private fun toggleVideoDetailsOpenIconRotation() {
        if (course_details_open_icon.rotation == 90F)
            course_details_open_icon.rotation = 0F
        else
            course_details_open_icon.rotation = 90F

        if (course_details_description.visibility == View.GONE)
            course_details_description.visibility = View.VISIBLE
        else
            course_details_description.visibility = View.GONE
    }


    private var courseDetails: JsonObject? = null
    private var videoList: JsonArray<JsonObject>? = null
    private var coursePurchased: JsonObject? = null

    private fun loadingData() {
        val back = MMfromServer(this, object : MMfromServerListener() {
            override fun onDownloadComplete(json: JsonObject) {
                when {
                    json["result"] == "success" -> courseDetails = json["data"] as JsonObject
                    else -> Log.d("MMDownload", "error")
                }
                checkIfAllLoaded()
            }
        })
        back.get(MMfromServer.REQUEST_COURSE_INFO, courseKey)
        back.execute()


        val back1 = MMfromServer(this, object : MMfromServerListener() {
            override fun onDownloadComplete(json: JsonObject) {
                when {
                    json["result"] == "success" -> videoList = json["data"] as JsonArray<JsonObject>
                    else -> Log.d("MMDownload", "error")
                }
                checkIfAllLoaded()
            }
        })
        back1.get(MMfromServer.REQUEST_VIDEO_LIST, courseKey)
        back1.execute()

        val back2 = MMfromServer(this, object : MMfromServerListener() {
            override fun onDownloadComplete(json: JsonObject) {
                when {
                    json["result"] == "success" -> coursePurchased = json["data"] as JsonObject
                    else -> Log.d("MMDownload", "error")
                }
                checkIfAllLoaded()
            }
        })
        back2.get(MMfromServer.REQUEST_USER_COURSE_PURCHASED, courseKey)
        back2.execute()
    }

    private var freeCourse = false
    private fun checkIfAllLoaded() {
        if (courseDetails != null && videoList != null && coursePurchased != null) {
//            println(courseDetails?.toJsonString(true))
//            println(coursePurchased?.toJsonString(true))
//            println(videoList?.toJsonString(true))

            println(courseDetails?.get("price"))
            freeCourse = (10 > (courseDetails?.get("price") as String).toInt())
            val purchased = if (coursePurchased?.get("purchased") as Boolean) 1 else null


            course_details_title.text = courseDetails?.get("name") as String
            course_details_price.text =
                    if (purchased == 1) "Purchased"
                    else
                        Util.formatNumber((courseDetails?.get("price") as String).toInt())
            course_details_description.text = courseDetails?.get("description") as String
            Glide
                    .with(applicationContext)
                    .load(courseDetails?.get("image") as String)
                    .apply(RequestOptions()
//                            .placeholder(R.color.colorPrimaryLight)
//                            .fitCenter()

//                            .crossFade()
                            .centerCrop())
                    .into(course_details_image)

            val videosList = arrayListOf<CourseVideo>()
            for (i in 0 until videoList!!.size) {
                val video = videoList!![i]
                videosList.add(CourseVideo(
                        (video["id"] as String).toInt(),
                        video["title"] as String,
                        video["description"] as String,
                        video["vimeo_video_id"] as String,
                        (video["course_id"] as String).toInt(),
                        purchased ?: (video["free"] as String).toInt(),
                        video["video_key"] as String
                ))
            }

            val adapter = CourseDetailsVideosAdapter(this, videosList)
            course_details_videos_recyclerview.adapter = adapter

            if (purchased == 1) {
                course_details_action_button.isEnabled = false
                course_details_action_button.text = "PURCHASED"
            }

            hideProgressBar()
        }
    }

    private fun hideProgressBar() {
        course_details_all.visibility = View.VISIBLE
        if (!freeCourse)
            course_details_action_button.visibility = View.VISIBLE
        course_details_progress_bar.visibility = View.GONE
    }
}
