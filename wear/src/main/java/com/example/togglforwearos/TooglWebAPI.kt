package com.example.togglforwearos

import android.util.Base64
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import android.util.JsonReader

import android.util.JsonToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

const val BASE_API_URL = "https://api.track.toggl.com/api/v8/"


class TooglWebAPI(val apiToken: String?) {


    suspend fun getProjectsList(): Int? {
        // Connecting to toggl API
        return withContext(Dispatchers.IO) {

            val auth = "$apiToken:api_token"

            val url = URL(BASE_API_URL + "me")
            val urlConnection: URLConnection = url.openConnection()
            urlConnection.setRequestProperty(
                "Authorization",
                "Basic " + Base64.encodeToString(
                    auth.toByteArray(StandardCharsets.UTF_8),
                    Base64.DEFAULT
                )
            )
            val inputStream: InputStream = urlConnection.getInputStream()


            @Throws(IOException::class)
            fun readJsonStream(`in`: InputStream?): Int? {
                val reader = JsonReader(InputStreamReader(`in`, "UTF-8"))
                try {
                    var since: Int = -1
                    reader.beginObject()
                    while (reader.hasNext()) {
                        val name = reader.nextName()
                        if (name == "since") {
                            since = reader.nextInt()
                        } else {
                            reader.skipValue()
                        }
                    }
                    reader.endObject()
                    return since
                } finally {
                    reader.close()
                }
            }

            return@withContext readJsonStream(inputStream)
        }
//        @Throws(IOException::class)
//        fun readMessagesArray(reader: JsonReader): List<Message?>? {
//            val messages: MutableList<Message?> = ArrayList<Message?>()
//            reader.beginArray()
//            while (reader.hasNext()) {
//                messages.add(readMessage(reader))
//            }
//            reader.endArray()
//            return messages
//        }
//
//        @Throws(IOException::class)
//        fun read(reader: JsonReader): List<Message?>? {
//            val messages: MutableList<Message?> = ArrayList<Message?>()
//            reader.beginArray()
//            while (reader.hasNext()) {
//                messages.add(readMessage(reader))
//            }
//            reader.endArray()
//            return messages
//        }
//
//        @Throws(IOException::class)
//        fun readUserInfo(reader: JsonReader): List<Message?>? {
//            val messages: MutableList<Message?> = ArrayList<Message?>()
//            reader.beginArray()
//            while (reader.hasNext()) {
//                messages.add(readMessage(reader))
//            }
//            reader.endArray()
//            return messages
//        }
//
//        @Throws(IOException::class)
//        fun readMessage(reader: JsonReader): Message? {
//            var id: Long = -1
//            var text: String? = null
//            var user: User? = null
//            var geo: List<Double?>? = null
//            reader.beginObject()
//            while (reader.hasNext()) {
//                val name = reader.nextName()
//                if (name == "id") {
//                    id = reader.nextLong()
//                } else if (name == "text") {
//                    text = reader.nextString()
//                } else if (name == "geo" && reader.peek() != JsonToken.NULL) {
//                    geo = readDoublesArray(reader)
//                } else if (name == "user") {
//                    user = readUser(reader)
//                } else {
//                    reader.skipValue()
//                }
//            }
//            reader.endObject()
//            return Message(id, text, user, geo)
//        }

//        @Throws(IOException::class)
//        fun readDoublesArray(reader: JsonReader): List<Double?>? {
//            val doubles: MutableList<Double?> = ArrayList()
//            reader.beginArray()
//            while (reader.hasNext()) {
//                doubles.add(reader.nextDouble())
//            }
//            reader.endArray()
//            return doubles
//        }
//
//        @Throws(IOException::class)
//        fun readUser(reader: JsonReader): User? {
//            var username: String? = null
//            var followersCount = -1
//            reader.beginObject()
//            while (reader.hasNext()) {
//                val name = reader.nextName()
//                if (name == "name") {
//                    username = reader.nextString()
//                } else if (name == "followers_count") {
//                    followersCount = reader.nextInt()
//                } else {
//                    reader.skipValue()
//                }
//            }
//            reader.endObject()
//            return User(username, followersCount)
//        }

    }


}