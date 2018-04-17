package com.magicalmethods.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.beust.klaxon.JsonObject
import com.magicalmethods.R
import com.magicalmethods.lib.MMfromServer
import com.magicalmethods.lib.Util
import com.magicalmethods.listeners.MMfromServerListener
import com.google.firebase.auth.FirebaseAuth
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_ticket.*
import kotlinx.android.synthetic.main.content_ticket.*

class TicketActivity : AppCompatActivity() {
    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create Ticket"

        val temp = Util.initDrawer(this)
        result = temp.component1()
        result.setSelection(4L, false)
        headerResult = temp.component2()

        val sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE)
        val displayName = sharedPreferences.getString("name", "")
        create_ticket__name.setText(displayName)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val email = user?.email ?: "default@example.com"
        create_ticket__email.setText(email)


        create_ticket__submit.setOnClickListener {
            it.isEnabled = false

            val ticketArgs = hashMapOf<String, String>()
            ticketArgs["name"] = create_ticket__name.text.toString()
            ticketArgs["email"] = create_ticket__email.text.toString()
            ticketArgs["phone"] = create_ticket__phone.text.toString()
            ticketArgs["subject"] = create_ticket__subject.text.toString()
            ticketArgs["message"] = create_ticket__message.text.toString()
            ticketArgs["android_id"] = Util.getAndroidId()

            val mmFromServer = MMfromServer(this@TicketActivity, object : MMfromServerListener() {
                override fun onDownloadComplete(json: JsonObject) {
                    when {
                        json["result"] == "success" -> {
                            AlertDialog.Builder(this@TicketActivity)
                                    .setMessage("Your message has been successfully sent.")
                                    .setCancelable(false)
                                    .setPositiveButton("Continue") { dialog, id ->
                                        startActivity(Intent(this@TicketActivity, HomeActivity::class.java))
//                                        it.isEnabled = true
                                    }
                                    .show()
                        }
                        json["result"] == "error" -> {
                            AlertDialog.Builder(this@TicketActivity)
                                    .setMessage("Error occurred: " + json["message"]).setCancelable(false)
                                    .setPositiveButton("Continue") { dialog, id ->
                                        it.isEnabled = true
                                    }
                                    .show()
                        }
                    }
                }
            })

            mmFromServer.get(MMfromServer.CREATE_TICKET)
            mmFromServer.ticketArgs(ticketArgs)
            mmFromServer.execute()
        }
    }
}
