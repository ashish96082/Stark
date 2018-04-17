package com.magicalmethods.handlers

import android.view.View
import android.widget.RadioGroup
import com.magicalmethods.entity.profile.CommonProfile
import com.magicalmethods.entity.profile.ParentsProfile
import com.magicalmethods.entity.profile.StudentProfile
import com.magicalmethods.entity.profile.TeacherProfile

/**
 * Created by ashish kumar
 * on 15-03-2018 | 03:26 PM.
 */
interface EditProfileHandler {
    fun onProfessionChanged(radioGroup: RadioGroup, int: Int)
    fun onSubmit(view: View, commonProfile: CommonProfile, studentProfile: StudentProfile,
                 parentsProfile: ParentsProfile, teacherProfile: TeacherProfile)
}