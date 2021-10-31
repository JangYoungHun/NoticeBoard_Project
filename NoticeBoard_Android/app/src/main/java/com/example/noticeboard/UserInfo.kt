package com.example.noticeboard

import android.content.Context
import android.content.SharedPreferences

class UserInfo(var context: Context) {
    var fileName = "UserInfo"

    var  sharedPreference = context.getSharedPreferences(fileName,0)

    fun setString(key:String, value:String){
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getString(key:String):String{
        return sharedPreference.getString(key,"")?:""
    }
}