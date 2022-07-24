package com.example.togglforwearos

import android.content.Context
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
import java.lang.Exception


const val BASE_API_URL = "https://api.track.toggl.com/api/v8/"

//
//open class TooglWebAPI(val apiToken: String) {
//
//    class WrongHttpResponseException(message: String,val code: Int) : IOException(message)
//
//    // get User's projects and recent time entries
//    suspend fun getUserData(): JSONObject? {
//        return fetchJSON(URL(BASE_API_URL + "me?with_related_data=true"))
//    }
//
//
//    suspend fun getCurrentTimeEntry(): JSONObject? {
//        return fetchJSON(URL(BASE_API_URL + "time_entries/current"))
//    }
//
//
//
//    protected open suspend fun fetchJSON(url: URL): JSONObject? {
//        return withContext(Dispatchers.IO) {
//            try {
//                return@withContext performJSONFetching(url)
//            }catch(e:Exception){
//                e.printStackTrace()
//                return@withContext null
//            }
//        }
//    }
//
//    // Auth to Toggl Track api and fetch a json
//    protected fun performJSONFetching(url: URL): JSONObject?{
//        val httpURLConnection = url.openConnection() as HttpURLConnection
//        httpURLConnection.setRequestProperty(
//            "Authorization",
//            "Basic " + Base64.encodeToString(
//                "$apiToken:api_token".toByteArray(StandardCharsets.UTF_8),
//                Base64.DEFAULT
//            )
//        )
//
//        val httpResponseCode = httpURLConnection.responseCode
//        if (httpResponseCode == 200) {
//            try {
//                val inputStream: InputStream = httpURLConnection.getInputStream()
//                try {
//                    val streamReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
//                    val responseStrBuilder = StringBuilder()
//                    var inputStr: String?
//                    while (streamReader.readLine()
//                            .also { inputStr = it } != null
//                    ) responseStrBuilder.append(inputStr)
//
//                    //returns the json object
//                    return JSONObject(responseStrBuilder.toString())
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            }
//        } else {
//            var errorMsg: String = "TogglWebAPI error: HTTP return code: $httpResponseCode."
//            if(httpResponseCode == 403){
//                errorMsg += " Check the correctness of your api token"
//            }
//            Log.e("Http request error", errorMsg)
//            throw WrongHttpResponseException(errorMsg, httpResponseCode)
//        }
//
//        //if something went wrong, return null
//        return null
//    }
//}
//
//
//class responsiveToToastTogglWebAPI(apiToken: String, val applicationContext: Context): TooglWebAPI(apiToken){
//    override suspend fun fetchJSON(url: URL): JSONObject? {
//        return withContext(Dispatchers.IO) {
//            try {
//                return@withContext performJSONFetching(url)
//            }catch(e:Exception){
//                withContext(Dispatchers.Main) {
//                    showToast(makeMyExceptionMessage(e),applicationContext)
//                }
//                e.printStackTrace()
//                return@withContext null
//            }
//        }
//    }
//}
//
//class debresponsiveToToastTogglWebAPI(apiToken: String, val applicationContext: Context): TooglWebAPI(apiToken){
//    override suspend fun fetchJSON(url: URL): JSONObject? {
//        return withContext(Dispatchers.IO) {
//                return@withContext performJSONFetching(url)
//
//        }
//    }
//}