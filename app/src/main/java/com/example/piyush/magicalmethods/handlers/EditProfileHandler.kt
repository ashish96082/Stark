package com.example.piyush.magicalmethods.handlers

import android.view.View
import android.widget.RadioGroup
import com.example.piyush.magicalmethods.entity.profile.CommonProfile
import com.example.piyush.magicalmethods.entity.profile.ParentsProfile
import com.example.piyush.magicalmethods.entity.profile.StudentProfile
import com.example.piyush.magicalmethods.entity.profile.TeacherProfile

/**
 * Created by ashish kumar
 * on 15-03-2018 | 03:26 PM.
 */
interface EditProfileHandler {
    fun onProfessionChanged(radioGroup: RadioGroup, int: Int)
    fun onSubmit(view: View, commonProfile: CommonProfile, studentProfile: StudentProfile,
                 parentsProfile: ParentsProfile, teacherProfile: TeacherProfile)
}