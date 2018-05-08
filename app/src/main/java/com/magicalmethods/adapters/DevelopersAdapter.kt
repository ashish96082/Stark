package com.magicalmethods.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.magicalmethods.R
import com.magicalmethods.entity.Developer

/**
 * Created by ashish kumar
 * on 18-03-2018 | 04:39 PM.
 */
class DevelopersAdapter(private val context: Context, private val developers: ArrayList<Developer>) : RecyclerView.Adapter<DevelopersAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_developers, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("asf", developers.size.toString())
        return developers.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder?.developerName?.text = developers[position].name
        holder?.developerEmail?.text = developers[position].email
        Log.d("asf", developers.size.toString())

        if (developers[position].profileLink != "")
            holder?.developerName?.setOnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(developers[position].profileLink)))
            }

        holder?.developerEmail?.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(developers[position].email))
            intent.type = "message/rfc822"
            (context as Activity).startActivityForResult(Intent.createChooser(intent, "Mail " + developers[position].name), 1)
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val developerName: TextView = view.findViewById(R.id.developer_name)
        val developerEmail: TextView = view.findViewById(R.id.developer_email)
    }
}