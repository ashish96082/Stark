package com.magicalmethods.entity.profile

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.magicalmethods.BR

/**
 * Created by ashish kumar
 * on 15-03-2018 | 08:01 AM.
 */

class StudentProfile : BaseObservable() {
//    enum class Standard {
//        ZERO {
//            override fun display() = "< 1"
//        },
//        ONE {
//            override fun display() = "1"
//        };
//
//        abstract fun display(): String
//    }

    var dob = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.dob)
        }

    var standard = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.standard)
        }

    var fatherName = ""
        @Bindable get
        set(value) {
            field = value.split(' ').joinToString(" ") { it.capitalize() }
            notifyPropertyChanged(BR.fatherName)
        }
}