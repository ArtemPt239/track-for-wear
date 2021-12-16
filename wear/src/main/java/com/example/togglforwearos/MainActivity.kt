package com.example.togglforwearos

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.togglforwearos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import kotlin.math.min

// Shared preferences keys
const val API_TOKEN_KEY = "api token key"
const val USER_INFO_KEY = "user info key"

const val NO_API_TOKEN_STRING = "no apy token here"

class MainActivity : Activity() {

    var userInfo: UserInfo? = null
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textView: TextView = findViewById(R.id.textView1)

        // Managing API token
        val sharedPref = getSharedPref(applicationContext)
        var togglAPIToken: String? = sharedPref.getString(API_TOKEN_KEY, null)

        val editTextAPIToken: EditText = findViewById(R.id.editTextAPIToken)
        editTextAPIToken.setText(togglAPIToken)
        val buttonAPIToken: Button = findViewById(R.id.buttonAPIToken)

        buttonAPIToken.setOnClickListener {
            togglAPIToken = editTextAPIToken.text.toString()
            with (sharedPref.edit()) {
                putString(API_TOKEN_KEY, togglAPIToken)
                apply()
            }
            if(userInfo != null){
                textView.text = userInfo!!.projects[0].name
            }
        }

        getAndStoreUserData(togglAPIToken)


        if(togglAPIToken != null && userInfo != null) {
            scope.launch {
                val tooglWebAPI = TooglWebAPI(togglAPIToken!!)
                val runningTimeEntry = tooglWebAPI.getCurrentTimeEntry()
                try {
                    val currentProject =
                        userInfo!!.projectsMap[runningTimeEntry?.getJSONObject("data")
                            ?.getString("pid")]
                    if (currentProject != null) {
                        val timeEntryStartTimestamp =
                            runningTimeEntry!!.getJSONObject("data").getString("start")
                        val diff: Duration = Duration.between(
                            convertStringToInstant(timeEntryStartTimestamp),
                            Instant.now()
                        )
                        textView.text = "${currentProject.name} ${convertDurationToString(diff)}"
                        textView.setBackgroundColor(currentProject.color)
                    }
                }catch (e: JSONException){
                    textView.text = resources.getString(R.string.no_current_timer)
                    textView.setBackgroundColor(Color.BLACK)
                }
            }
        }

    }


    fun convertDurationToString(indiff: Duration): String{
        var diff = indiff
        fun getSubstringBetween(string: String, start: String, end: String): String{
            return string.split(start)[1].split(end)[0]
        }
        val days = diff.toDays()
        diff = diff.minusDays(days)
        val hours = diff.toHours()
        diff = diff.minusHours(hours)
        val minutes = diff.toMinutes()
        diff = diff.minusMinutes(minutes)
        var seconds = getSubstringBetween(diff.toString(), "PT", ".")
        if (seconds.length == 1){
            seconds = "0$seconds"
        }
        return if(diff.toHours() == 0L){
            "$minutes:$seconds"
        }else{
            "$hours:$minutes:$seconds"
        }
    }


    //
    fun getAndStoreUserData(togglAPIToken: String?){
        val sharedPref = getSharedPref(applicationContext)
        var userInfoString: String? = sharedPref.getString(USER_INFO_KEY, null)
        if(userInfoString != null){
            userInfo = UserInfo(JSONObject(userInfoString))
        }

        scope.launch {
            if(togglAPIToken != null) {
                val newUserInfoJSONObject = TooglWebAPI(togglAPIToken).getUserData()
                if(newUserInfoJSONObject != null && newUserInfoJSONObject.toString() != userInfoString){
                    userInfo = UserInfo(newUserInfoJSONObject)
                    with (sharedPref.edit()) {
                        putString(USER_INFO_KEY, userInfo?.json.toString())
                        apply()
                    }
                }
            }
        }
    }


    fun showToast(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}
