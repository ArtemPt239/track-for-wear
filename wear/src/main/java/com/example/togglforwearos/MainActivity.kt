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
import com.example.togglforwearos.dataLayer.APITokenRepository
import com.example.togglforwearos.databinding.ActivityMainBinding
import kotlinx.coroutines.*
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

const val NO_API_TOKEN_STRING = "no apy token here"

class MainActivity : Activity() {



    var userInfo: UserInfo? = null
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiTokenRepository = APITokenRepository(applicationContext)

        val textView: TextView = findViewById(R.id.textView1)

        // Managing API token
        val sharedPref = getSharedPref(applicationContext)

        val editTextAPIToken: EditText = findViewById(R.id.editTextAPIToken)
        try {
            editTextAPIToken.setText(apiTokenRepository.togglAPIToken)
        }catch(e: APITokenRepository.NoAPITokenFoundException){
            ;
        }


        val buttonAPIToken: Button = findViewById(R.id.buttonAPIToken)
        buttonAPIToken.setOnClickListener {
            apiTokenRepository.togglAPIToken = editTextAPIToken.text.toString()
        }

        scope.launch() {
            try {
                getAndStoreUserData(apiTokenRepository.togglAPIToken)

                if (userInfo != null) {
                    val tooglWebAPI = responsiveToToastTogglWebAPI(apiTokenRepository.togglAPIToken!!, applicationContext)
                    val runningTimeEntry = tooglWebAPI.getCurrentTimeEntry()
                    try {
                        val currentProject =
                            userInfo!!.projectsMap[runningTimeEntry?.getJSONObject("data")
                                ?.getString("pid")]
                        if (currentProject != null) {
                            val runningTimeEntry =
                                TimeEntry(
                                    runningTimeEntry!!.getJSONObject("data"),
                                    currentProject
                                )

                            textView.text =
                                "${runningTimeEntry.projectName} ${
                                    durationSecondsToString(
                                        Instant.now().epochSecond - runningTimeEntry.startTimeEpoch
                                    )
                                }"
                            textView.setBackgroundColor(currentProject.color)
                        }
                    } catch (e: JSONException) {
                        textView.text = resources.getString(R.string.no_current_timer)
                        textView.setBackgroundColor(Color.BLACK)
                    }

                }
            } catch (e: TooglWebAPI.WrongHttpResponseException) {
                textView.text = makeMyExceptionMessage(e)
                textView.setBackgroundColor(Color.BLACK)
                e.printStackTrace()
            }
        }


    }




    //
    fun getAndStoreUserData(togglAPIToken: String?) {
        val sharedPref = getSharedPref(applicationContext)
        var userInfoString: String? = sharedPref.getString(USER_INFO_KEY, null)
        if (userInfoString != null) {
            userInfo = UserInfo(JSONObject(userInfoString))
        }

        scope.launch {
            if (togglAPIToken != null) {
                val newUserInfoJSONObject = responsiveToToastTogglWebAPI(togglAPIToken, applicationContext).getUserData()
                if (newUserInfoJSONObject != null && newUserInfoJSONObject.toString() != userInfoString) {
                    userInfo = UserInfo(newUserInfoJSONObject)
                    with(sharedPref.edit()) {
                        putString(USER_INFO_KEY, userInfo?.json.toString())
                        apply()
                    }
                }
            }
        }
    }
}
