package com.magicalmethods.lib

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.provider.Settings
import com.magicalmethods.R
import com.magicalmethods.activity.*
import com.magicalmethods.entity.profile.CommonProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.NumberFormat
import java.util.*


/**
 * Created by ashish kumar on 28-12-2017 | 05:27 PM.
 */
class Util {
    companion object {
        fun md5(message: String): String {
            return try {
                // Creating md5 hash
                val digest = MessageDigest.getInstance("MD5")
                digest.update(message.toByteArray())
                val messageDigest = digest.digest()

                // Creating hex string from message digest
                val hexString = StringBuffer()
                for (i in 0..(messageDigest.size - 1)) {
                    val hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
                    if (hex.length == 1)
                        hexString.append("0")
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                ""
            }
        }

        /**
         * Returns version of the app installed
         * @return Int
         */
        fun getVersion(context: Context): Int {
            return try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                0
            }
        }

        /**
         * Time passed since epoch
         * in seconds
         */
        fun getCurrentTimestamp(): Long {
            val date = Calendar.getInstance().time;
            return date.time
        }

        /**
         * Returns android id
         * No more recommended by google
         * use instance id instead
         */
        fun getAndroidId(): String {
            return Settings.Secure.ANDROID_ID
        }

        fun getInstanceId(): String {
            // @TODO Replace Android id with instance id
            return ""
        }

        /**
         * Returns full country name of the user based on configuration
         * Saves the country in preference manager for further calling
         */
        fun getCountry(context: Context): String {
            var country = PreferenceManager.getDefaultSharedPreferences(context).getString("country", "DEFAULT")
            if (country != null)
                return country

            // @TODO use location manager, telephony manager, networks' providers for finding user country
            country = context.resources.configuration.locale.country
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("country", country).apply()
            // @TODO Create a class to handle preference manager
            return country
        }

        fun getState() {
            // @TODO Implement getstate
        }


        fun initDrawer(context: Context): Pair<Drawer, AccountHeader> {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val email = user?.email ?: "contact@example.com"

            var profile = CommonProfile()
            val sharedPreferences = context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, MODE_PRIVATE)
            val profileString = sharedPreferences.getString(Constants.SHAREDPREFERENCE_COMMON_PROFILE, "")
            if (profileString != "")
                profile = Gson().fromJson<CommonProfile>(profileString, CommonProfile::class.java)

            val profileDrawerItem = ProfileDrawerItem().withName(profile.name).withEmail(email).withIcon(context.getResources().getDrawable(R.drawable.icon__default_profile))


            when (profile.gender) {
                CommonProfile.Gender.MALE.name -> profileDrawerItem.withIcon(context.resources.getDrawable(R.drawable.icon__default_profile_male))
                CommonProfile.Gender.FEMALE.name -> profileDrawerItem.withIcon(context.resources.getDrawable(R.drawable.icon__default_profile_female))
                else -> profileDrawerItem.withIcon(context.resources.getDrawable(R.drawable.icon__default_profile))
            }

            // Create the AccountHeader
            val accountHeaderBuilder = AccountHeaderBuilder()
                    .withActivity(context as Activity)
                    .withHeaderBackground(R.drawable.header)
                    .addProfiles(
                            profileDrawerItem
                    )
                    .withSelectionListEnabledForSingleProfile(false)
                    .withOnAccountHeaderSelectionViewClickListener { view, profile ->
                        context.startActivity(Intent(context, EditProfileActivity::class.java))
                        false
                    }
            val headerResult = accountHeaderBuilder.build()


            val result = DrawerBuilder()
                    .withActivity(context)
                    .withAccountHeader(headerResult)
                    .withToolbar(context.findViewById(R.id.toolbar))
                    .withActionBarDrawerToggleAnimated(true)

                    .addDrawerItems(
                            PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(context.getResources().getDrawable(R.drawable.home)),
                            PrimaryDrawerItem().withIdentifier(2).withName("Explore").withIcon(context.getResources().getDrawable(R.drawable.explore)),

//                            ExpandableDrawerItem().withName("Your courses").withIcon(context.getResources().getDrawable(R.drawable.your_courses)).withIdentifier(3).withSelectable(false).withSubItems(
//                                    SecondaryDrawerItem().withName("Course 1").withLevel(2).withIdentifier(31).withSelectable(false),
//                                    SecondaryDrawerItem().withName("Course 2").withLevel(2).withIdentifier(32).withSelectable(false)
//                            ),
                            PrimaryDrawerItem().withIdentifier(3).withName("My Courses").withIcon(context.resources.getDrawable(R.drawable.your_courses)),

//                            PrimaryDrawerItem().withIdentifier(4).withName("Help and Feedback").withIcon(context.getResources().getDrawable(R.drawable.help_and_feedback)),
                            PrimaryDrawerItem().withIdentifier(4).withName("Enquiry with Us").withIcon(context.getResources().getDrawable(R.drawable.icon__contact_mail)),
                            PrimaryDrawerItem().withIdentifier(5).withName("About Us").withIcon(context.getResources().getDrawable(R.drawable.icon__information))


                    )
                    .withOnDrawerItemClickListener { view, position, drawerItem ->
                        if (drawerItem != null) {
                            val intent = when {
                                drawerItem.identifier == 1L -> Intent(context, HomeActivity::class.java)
                                drawerItem.identifier == 2L -> Intent(context, ExploreActivity::class.java)
                                drawerItem.identifier == 3L -> Intent(context, PurchasedCoursesActivity::class.java)
//                                drawerItem.identifier == 4L -> Intent(context, HelpAndFeedbackActivity::class.java)
                                drawerItem.identifier == 4L -> Intent(context, TicketActivity::class.java)
                                drawerItem.identifier == 5L -> Intent(context, AboutUsActivity::class.java)
                                else -> null
                            }
                            if (intent != null) {
                                context.startActivity(intent)
                            }
                        }

                        false
                    }
                    // .withSavedInstance(savedInstanceState)
                    // .withShowDrawerOnFirstLaunch(true)
                    .build()

            return result to headerResult
        }

        fun saveUserData(context: Context) {
            val sharedPreferences = context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, MODE_PRIVATE)

            if (!sharedPreferences.contains(Constants.SHAREDPREFERENCE_COMMON_PROFILE)) {
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser

                val database = FirebaseDatabase.getInstance()
                val reference = database.reference.child(Constants.FIREBASE_PROFILE).child(user?.uid).child(Constants.FIREBASE_COMMON_PROFILE)

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        val profile = p0?.getValue(CommonProfile::class.java)

                        if (profile == null || profile.name == "") {
                            AlertDialog.Builder(context)
                                    .setMessage("It seems you have not completed profile. Please do so to continue.")
                                    .setPositiveButton("Visit Profile") { dialogInterface, i ->
                                        context.startActivity(Intent(context, EditProfileActivity::class.java))
                                    }
                                    .setCancelable(false)
                                    .show()
                            return
                        }

                        val editor = sharedPreferences.edit()
                        editor.putString(Constants.SHAREDPREFERENCE_COMMON_PROFILE, Gson().toJson(profile))
                        editor.apply()
                    }
                })
            }
        }

        fun formatNumber(_number: Int): String {
            val number = _number.toString()

            val numberFormat = NumberFormat.getCurrencyInstance(Locale("hi", "in"))
            val symbol = numberFormat.format(0.00).replace("0.00", "")
            return numberFormat.format(number.toDouble()).replace(symbol, symbol + " ")
        }
    }
}