package com.example.togglforwearos

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.togglforwearos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        textView.text = "test"

        // Managing API token
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
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
                val currentProject = userInfo!!.projectsMap[tooglWebAPI.getCurrentTimeEntry()?.getJSONObject("data")?.getString("pid")]
                if(currentProject != null) {
                    textView.text = currentProject.name
                    textView.setBackgroundColor(currentProject.color)
                }
            }
        }
    }


    //
    fun getAndStoreUserData(togglAPIToken: String?){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        var userInfoString: String? = sharedPref.getString(USER_INFO_KEY, null)
        if(userInfoString != null){
            userInfo = UserInfo(JSONObject(userInfoString))
        }

        scope.launch {
            if(togglAPIToken != null) {
                val newUserInfoJSONObject = TooglWebAPI(togglAPIToken).getUserData()
                if(newUserInfoJSONObject != null && newUserInfoJSONObject.toString() != userInfoString){
                    showToast(newUserInfoJSONObject.toString())
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
