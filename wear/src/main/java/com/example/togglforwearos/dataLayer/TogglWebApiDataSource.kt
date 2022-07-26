package com.example.togglforwearos.dataLayer

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets


/**
 * Handles requests to the toggle web api and returns results as JSONObjects
 */
interface TogglWebApiDataSourceInterface {
    suspend fun getRunningTimer(): JSONObject
    suspend fun getUserInfo(): JSONObject
}


const val BASE_API_URL = "https://api.track.toggl.com/api/v8/"

class TogglWebApi(val apiToken: String) : TogglWebApiDataSourceInterface {
    init {
        if (apiToken.isEmpty()) {
            throw Exception("Api Token <token>${apiToken}</token> is empty or null")
        }
    }


    class WrongHttpResponseException(message: String, val code: Int) : IOException(message)

    // get User's projects and recent time entries
    override suspend fun getUserInfo(): JSONObject {
        return fetchJSON(URL(com.example.togglforwearos.BASE_API_URL + "me?with_related_data=true"))
    }


    override suspend fun getRunningTimer(): JSONObject {
        return fetchJSON(URL(com.example.togglforwearos.BASE_API_URL + "time_entries/current"))
    }


    protected suspend fun fetchJSON(url: URL): JSONObject {
        return withContext(Dispatchers.IO) {
            val result = performJSONFetching(url)
            if (result != null) {
                return@withContext result
            } else {
                throw NullPointerException("Returned JSON is null")
            }
        }
    }

    // Auth to Toggl Track api and fetch a json
    protected fun performJSONFetching(url: URL): JSONObject? {
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.setRequestProperty(
            "Authorization",
            "Basic " + Base64.encodeToString(
                "$apiToken:api_token".toByteArray(StandardCharsets.UTF_8),
                Base64.DEFAULT
            )
        )

        val httpResponseCode = httpURLConnection.responseCode
        if (httpResponseCode == 200) {
            try {
                val inputStream: InputStream = httpURLConnection.getInputStream()
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
        } else {
            var errorMsg: String = "TogglWebAPI error: HTTP return code: $httpResponseCode."
            if (httpResponseCode == 403) {
                errorMsg += " Check the correctness of your api token"
            }
            Log.e("Http request error", errorMsg)
            throw WrongHttpResponseException(errorMsg, httpResponseCode)
        }

        //if something went wrong, return null
        return null
    }
}