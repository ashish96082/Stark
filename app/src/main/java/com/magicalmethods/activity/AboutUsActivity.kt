package com.magicalmethods.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.magicalmethods.R
import com.magicalmethods.adapters.DevelopersAdapter
import com.magicalmethods.entity.Developer
import com.magicalmethods.lib.Util
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

        val developers = arrayListOf<Developer>()
        developers.add(Developer("Pratyush Ranjan", "pratyush11197@gmail.com", "https://www.linkedin.com/in/pratyush-ranjan-294585137/"))
        developers.add(Developer("Piyush Kumar", "piyushesquire@gmail.com", "https://www.linkedin.com/in/piyush-kumar-921b02130/"))
        developers.add(Developer("Sweta Rani", "swetarani1110@gmail.com", "https://www.linkedin.com/in/sweta-rani-845587137/"))
        developers.add(Developer("Kritika Kumari", "kritika.kriti.145@gmail.com", "https://www.linkedin.com/in/kritika-kumari-520587137/"))
        developers.add(Developer("Jyoti Verma", "jyotiverma2806@yahoo.com", "https://www.linkedin.com/in/jyoti-verma-144544139/"))
        developers.add(Developer("Ashish Kumar", "contact@ashishkr.net", "http://ashishkr.net/"))

        recylerview_developers.layoutManager = LinearLayoutManager(this)
        recylerview_developers.adapter = DevelopersAdapter(this, developers)
        recylerview_developers.isNestedScrollingEnabled = false
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
