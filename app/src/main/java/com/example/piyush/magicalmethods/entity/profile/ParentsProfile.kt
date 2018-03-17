package com.example.piyush.magicalmethods.entity.profile

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.example.piyush.magicalmethods.BR

/**
 * Created by ashish kumar
 * on 15-03-2018 | 08:12 AM.
 */

class ParentsProfile : BaseObservable() {
    var childrensClass = ""
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.childrensClass)
        }
}