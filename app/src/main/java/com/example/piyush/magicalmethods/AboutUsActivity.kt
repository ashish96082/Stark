package com.example.piyush.magicalmethods

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.piyush.magicalmethods.activity.TicketActivity
import com.example.piyush.magicalmethods.lib.Util
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.content_about_us.*

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "About Us"

        val temp = Util.initDrawer(this)
        val result = temp.component1()
        result.setSelection(5L, false)
        val headerResult = temp.component2()

//        phone.setOnClickListener {
//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse("tel:239482349")
//            this@AboutUsActivity.startActivity(callIntent)
//        }

        contact_us.setOnClickListener {
            startActivity(Intent(this@AboutUsActivity, TicketActivity::class.java))
        }
    }

}
