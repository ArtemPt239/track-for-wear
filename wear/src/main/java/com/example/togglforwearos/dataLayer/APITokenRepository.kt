package com.example.togglforwearos.dataLayer

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.togglforwearos.API_TOKEN_KEY
import java.lang.Exception

const val API_TOKEN_KEY = "api token key"

class APITokenRepository(val context: Context) {
    class NoAPITokenFoundException(): Exception()

    var togglAPIToken:String = ""
        get() {
            val buf = getDefaultSharedPreferences(context).getString(API_TOKEN_KEY, null)
            if (buf != null){
                return buf
            }else{
                throw NoAPITokenFoundException()
            }
        }
        set(value) {
            field = value
            with(getDefaultSharedPreferences(context).edit()) {
                putString(API_TOKEN_KEY, value)
                apply()
            }
        }
}