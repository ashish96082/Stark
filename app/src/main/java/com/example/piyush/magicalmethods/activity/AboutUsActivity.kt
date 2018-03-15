package com.example.piyush.magicalmethods.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.piyush.magicalmethods.R
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

        phone.setOnClickListener {
            callNumber("+919971635323")
        }

        landline.setOnClickListener {
            callNumber("0124-4036302")
        }

        contact_us.setOnClickListener {
            startActivity(Intent(this@AboutUsActivity, TicketActivity::class.java))
        }
    }

    private fun callNumber(number: String): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        return if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    12412
            )
            false
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + number)
            startActivity(callIntent)

            true
        }
    }
}
