package com.example.togglforwearos

import android.util.Base64
import android.util.Log
import java.net.URL

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import org.json.JSONException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.io.*


const val BASE_API_URL = "https://api.track.toggl.com/api/v8/"


class TooglWebAPI(val apiToken: String) {


    // get User's projects and recent time entries
    suspend fun getUserData(): JSONObject? {
        return withContext(Dispatchers.IO) {
            return@withContext fetchJSON(URL(BASE_API_URL + "me?with_related_data=true"))
        }
    }


    suspend fun getCurrentTimeEntry(): JSONObject? {
        return withContext(Dispatchers.IO) {
            return@withContext fetchJSON(URL(BASE_API_URL + "time_entries/current"))
        }
    }


    // Auth to Toggl Track api and fetch a json
    private fun fetchJSON (url: URL): JSONObject?{
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestProperty(
            "Authorization",
            "Basic " + Base64.encodeToString(
                "$apiToken:api_token".toByteArray(StandardCharsets.UTF_8),
                Base64.DEFAULT
            )
        )

        val httpResponseCode = urlConnection.responseCode
        if(httpResponseCode == 200) {
            try {
                val inputStream: InputStream = urlConnection.getInputStream()
                try {
                    val streamReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
                    val responseStrBuilder = StringBuilder()
                    var inputStr: String?
                    while (streamReader.readLine()
                            .also { inputStr = it } != null
                    ) responseStrBuilder.append(inputStr)

                    //returns the json object
                    return JSONObject(responseStrBuilder.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }else{
            Log.e("Http request error", "HTTP return code: $httpResponseCode")
        }

//        }
        //if something went wrong, return null
        return null
    }
}