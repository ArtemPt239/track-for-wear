package com.example.togglforwearos

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.togglforwearos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val API_TOKEN_KEY = "api token key"
const val NO_API_TOKEN_STRING = "no apy token here"

class MainActivity : Activity() {

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
        var togglAPIToken: String? = sharedPref.getString(API_TOKEN_KEY, NO_API_TOKEN_STRING)

        val editTextAPIToken: EditText = findViewById(R.id.editTextAPIToken)
        editTextAPIToken.setText(togglAPIToken)
        val buttonAPIToken: Button = findViewById(R.id.buttonAPIToken)

        buttonAPIToken.setOnClickListener {
            togglAPIToken = editTextAPIToken.text.toString()
            with (sharedPref.edit()) {
                putString(API_TOKEN_KEY, togglAPIToken)
                apply()
            }
        }





        //
        scope.launch {
            val tooglWebAPI = TooglWebAPI(togglAPIToken)
            textView.text = tooglWebAPI.getCurrentTimeEntry().toString()
        }

    }


    fun getAndStoreUserData(){

    }
}
