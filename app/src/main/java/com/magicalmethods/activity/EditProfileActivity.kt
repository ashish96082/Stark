package com.magicalmethods.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.magicalmethods.R
import com.magicalmethods.adapters.NoFilterArrayAdapter
import com.magicalmethods.databinding.ActivityEditProfileBinding
import com.magicalmethods.entity.profile.CommonProfile
import com.magicalmethods.entity.profile.ParentsProfile
import com.magicalmethods.entity.profile.StudentProfile
import com.magicalmethods.entity.profile.TeacherProfile
import com.magicalmethods.handlers.EditProfileHandler
import com.magicalmethods.lib.Constants
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.content_edit_profile.*
import java.text.SimpleDateFormat
import java.util.*


class EditProfileActivity : AppCompatActivity(), EditProfileHandler {
    override fun onSubmit(view: View, commonProfile: CommonProfile, studentProfile: StudentProfile,
                          parentsProfile: ParentsProfile, teacherProfile: TeacherProfile) {
        println(commonProfile.toString())
        println(studentProfile.toString())
        println(parentsProfile.toString())
        println(teacherProfile.toString())


        refrence.child(Constants.FIREBASE_COMMON_PROFILE).setValue(commonProfile)
        refrence.child(Constants.FIREBASE_STUDENT_PROFILE).setValue(studentProfile)
        refrence.child(Constants.FIREBASE_PARENTS_PROFILE).setValue(parentsProfile)
        refrence.child(Constants.FIREBASE_TEACHER_PROFILE).setValue(teacherProfile)

        saveCommonProfileLocally(commonProfile)

        AlertDialog.Builder(this@EditProfileActivity)
                .setMessage("Profile saved successfully")
                .setCancelable(false)
                .setPositiveButton("Continue", { dialogInterface, i ->
                    startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                })
                .show()
    }

    private fun saveCommonProfileLocally(commonProfile: CommonProfile) {
        val sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.SHAREDPREFERENCE_COMMON_PROFILE, Gson().toJson(commonProfile))
        editor.apply()
    }

    override fun onProfessionChanged(radioGroup: RadioGroup, int: Int) {
        println(int)
        when (int) {
            R.id.edit_profile__profession_student -> {
                commonProfile.profession = CommonProfile.Profession.STUDENT.name
                student_profile.visibility = View.VISIBLE
                parents_profile.visibility = View.GONE
                teacher_profile.visibility = View.GONE
            }
            R.id.edit_profile__profession_parent -> {
                commonProfile.profession = CommonProfile.Profession.PARENTS.name
                student_profile.visibility = View.GONE
                parents_profile.visibility = View.VISIBLE
                teacher_profile.visibility = View.GONE
            }
            R.id.edit_profile__profession_teacher -> {
                commonProfile.profession = CommonProfile.Profession.TEACHER.name
                student_profile.visibility = View.GONE
                parents_profile.visibility = View.GONE
                teacher_profile.visibility = View.VISIBLE
            }
        }
    }

    private fun initDatabase() {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        firebaseUser = firebaseAuth.currentUser
        uid = firebaseUser?.uid
        refrence = database.reference.child(Constants.FIREBASE_PROFILE).child(uid)

        refrence.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
//                println(p0?.value)
                progressDialog.dismiss()

//                println(commonProfile)
                commonProfile = p0?.child(Constants.FIREBASE_COMMON_PROFILE)?.getValue(CommonProfile::class.java) ?: CommonProfile()
                studentProfile = p0?.child(Constants.FIREBASE_STUDENT_PROFILE)?.getValue(StudentProfile::class.java) ?: StudentProfile()
                teacherProfile = p0?.child(Constants.FIREBASE_TEACHER_PROFILE)?.getValue(TeacherProfile::class.java) ?: TeacherProfile()
                parentsProfile = p0?.child(Constants.FIREBASE_PARENTS_PROFILE)?.getValue(ParentsProfile::class.java) ?: ParentsProfile()

                binding.commonProfile = commonProfile
                when (commonProfile.profession) {
                    CommonProfile.Profession.STUDENT.name -> (edit_profile__profession_group.getChildAt(0) as RadioButton)
                            .isChecked = true
                    CommonProfile.Profession.PARENTS.name -> (edit_profile__profession_group.getChildAt(1) as RadioButton)
                            .isChecked = true
                    CommonProfile.Profession.TEACHER.name -> (edit_profile__profession_group.getChildAt(2) as RadioButton)
                            .isChecked = true
                }
                binding.studentProfile = studentProfile
                binding.teacherProfile = teacherProfile
                binding.parentsProfile = parentsProfile
            }

        })
    }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var refrence: DatabaseReference
    private var firebaseUser: FirebaseUser? = null
    private var uid: String? = null

    private var commonProfile = CommonProfile()
    private var studentProfile = StudentProfile()
    private var parentsProfile = ParentsProfile()
    private var teacherProfile = TeacherProfile()

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_edit_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Profile")
        progressDialog.show()

        initDatabase()

        binding.commonProfile = commonProfile
        binding.studentProfile = studentProfile
        binding.parentsProfile = parentsProfile
        binding.teacherProfile = teacherProfile
        binding.handler = this

        val genders = CommonProfile.Gender.values().map { it.name }
        val adapter = NoFilterArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, genders)



        edit_profile__gender.setAdapter(adapter)
        edit_profile__gender.keyListener = null
        edit_profile__gender.setOnTouchListener { view, motionEvent ->
            (view as AutoCompleteTextView).showDropDown()
            false
        }

        val calender = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, monthOfYear)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = "dd/MM/yy"
            val simpleDateFormat = SimpleDateFormat(format, Locale("hi", "in"))
            edit_profile__date_of_birth.setText(simpleDateFormat.format(calender.time))
        }

        edit_profile__date_of_birth.setOnClickListener {
            DatePickerDialog(this@EditProfileActivity, date, calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH))
                    .show()
        }

        val classes = listOf("< 1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "> 12")
        val classAdapter = NoFilterArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, classes)
        edit_profile__class.setAdapter(classAdapter)
        edit_profile__class.keyListener = null
        edit_profile__class.setOnTouchListener { view, motionEvent ->
            (view as AutoCompleteTextView).showDropDown()
            false
        }

        edit_profile__children_class.setAdapter(classAdapter)
        edit_profile__children_class.keyListener = null
        edit_profile__children_class.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }


//        edit_profile__save.setOnClickListener {
//            println(commonProfile.toString())
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
