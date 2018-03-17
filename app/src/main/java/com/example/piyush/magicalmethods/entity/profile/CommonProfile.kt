package com.example.piyush.magicalmethods.entity.profile

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.piyush.magicalmethods.BR

/**
 * Created by ashish kumar
 * on 15-03-2018 | 07:57 AM.
 */
class CommonProfile : BaseObservable() {
    enum class Gender {
        MALE, FEMALE, OTHER
    }

    enum class Profession {
        STUDENT, PARENTS, TEACHER
    }

//    @get:Bindable
//    @set:Bindable
    var name: String = ""
        @Bindable get
        set(value) {
            field = value.split(' ').joinToString(" ") { it.capitalize() }
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    @set:Bindable
    var address: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.address)
        }

    @get:Bindable
    @set:Bindable
    var gender: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.gender)
        }

    @get:Bindable
    @set:Bindable
    var phone: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phone)
        }

    @get:Bindable
    @set:Bindable
    var profession: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.profession)
        }

    override fun toString(): String {
        return "CommonProfile(name='$name', address='$address', gender='$gender', phone='$phone', profession='$profession')"
    }
}