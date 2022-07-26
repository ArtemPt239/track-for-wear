package com.example.togglforwearos

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.togglforwearos.dataLayer.APITokenRepository
import com.example.togglforwearos.dataLayer.TogglRepository
import com.example.togglforwearos.dataLayer.TogglWebApi
import com.example.togglforwearos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import java.time.Instant


class MainActivity : Activity() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiTokenRepository = APITokenRepository(applicationContext)

        val textView: TextView = findViewById(R.id.textView1)

        // Managing API token
        val editTextAPIToken: EditText = findViewById(R.id.editTextAPIToken)
        try {
            editTextAPIToken.setText(apiTokenRepository.togglAPIToken)
        } catch (e: APITokenRepository.NoAPITokenFoundException) {
            ;
        }


        val buttonAPIToken: Button = findViewById(R.id.buttonAPIToken)
        buttonAPIToken.setOnClickListener {
            apiTokenRepository.togglAPIToken = editTextAPIToken.text.toString()
        }

        scope.launch() {
            try {
                try {
                    val togglRepository = TogglRepository(applicationContext)
                    togglRepository.updateUserInfo()
                    val runningTimeEntry = togglRepository.getRunningTimer()
                    textView.text =
                        runningTimeEntry.projectName + " " + durationSecondsToString(
                            Instant.now().epochSecond - runningTimeEntry.startTimeEpoch
                        )
                    textView.setBackgroundColor(runningTimeEntry.projectColor)

                    // refresh every second
                    val thread: Thread = object : Thread() {
                        override fun run() {
                            try {
                                while (!this.isInterrupted) {
                                    sleep(1000)
                                    runOnUiThread {
                                        textView.text =
                                            runningTimeEntry.projectName + " " + durationSecondsToString(
                                                Instant.now().epochSecond - runningTimeEntry.startTimeEpoch
                                            )
                                    }
                                }
                            } catch (e: InterruptedException) {
                            }
                        }
                    }

                    thread.start()
                } catch (e: JSONException) {
                    textView.text = resources.getString(R.string.no_current_timer)
                    textView.setBackgroundColor(Color.BLACK)
                    showToast("Error: " + makeMyExceptionMessage(e), applicationContext)
                }
            } catch (e: TogglWebApi.WrongHttpResponseException) {
                textView.text = makeMyExceptionMessage(e)
                textView.setBackgroundColor(Color.BLACK)
                e.printStackTrace()
            }
        }



    }
}
