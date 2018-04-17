package com.magicalmethods.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.magicalmethods.R
import com.magicalmethods.adapters.CourseDetailsAdapter
import com.magicalmethods.entity.CourseDetails
import com.magicalmethods.lib.Util
import com.magicalmethods.listeners.MMfromServerListener
import com.magicalmethods.lib.MMfromServer
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer

class PurchasedCoursesActivity : AppCompatActivity() {

    internal lateinit var toolbar: Toolbar
    internal var result: Drawer? = null
    internal var headerResult: AccountHeader? = null
    private lateinit var recyclerView: RecyclerView
    private var gridLayoutManager: GridLayoutManager? = null
    private var adapter: CourseDetailsAdapter? = null
    private var data_list: ArrayList<CourseDetails>? = null
    internal lateinit var mLoadingProgress: ProgressBar
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_courses)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "My Courses"
        mLoadingProgress = findViewById(R.id.pb_loading)
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        data_list = ArrayList()
        //        load_data_from_server(0);
        loadData()


        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        adapter = CourseDetailsAdapter(this, arrayListOf())
        recyclerView.setAdapter(adapter)


        val temp = Util.initDrawer(this)
        result = temp.component1()
        result?.setSelection(3L, false)
        headerResult = temp.component2()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.card_list_menu, menu)
        return true
    }

    private fun loadData() {
        val task = MMfromServer(this, object : MMfromServerListener() {
            override fun onDownloadComplete(json: JsonObject) {
                println(json.toJsonString(true))

                //                try {
                val courses = ArrayList<CourseDetails>()
                val result = json["result"] as String?
                if (result == "success") {
                    val array = json["data"] as JsonArray<*>?

                    for (i in array!!.indices) {
                        val `object` = array[i] as JsonObject

                        val data = CourseDetails(
                                (`object`["name"] as String?)!!,
                                (`object`["description"] as String?)!!,
                                (`object`["image"] as String?)!!,
                                Integer.parseInt(`object`["price"] as String?),
                                (`object`["course_key"] as String?)!!
                        )
                        courses.add(data)
                    }
                }

                val error_msg = findViewById<TextView>(R.id.error_msg)
                mLoadingProgress.visibility = View.INVISIBLE
                if (data_list == null) {
                    error_msg.visibility = View.VISIBLE
                    recyclerView!!.setVisibility(View.INVISIBLE)
                } else {
                    recyclerView!!.setVisibility(View.VISIBLE)
                }
                adapter!!.addData(courses)
            }

            override fun onPreDownload() {
                mLoadingProgress.visibility = View.VISIBLE
            }
        })
        task.get(MMfromServer.REQUEST_USER_COURSE_LIST, "asaouetsfnvpsq")
        task.execute()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var outState = outState
        outState = result!!.saveInstanceState(outState)
        outState = headerResult!!.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        if (result != null && result!!.isDrawerOpen) {
            result!!.closeDrawer()
        } else {
            super.onBackPressed()
        }

    }
}
