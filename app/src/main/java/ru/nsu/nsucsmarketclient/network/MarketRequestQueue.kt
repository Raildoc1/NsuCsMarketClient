package ru.nsu.nsucsmarketclient.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule

class MarketRequestQueue {
    private val minRequestTimeDelta : Long = 1000;
    private val queue: Queue<MarketRequest> = LinkedList();
    private val timer = Timer("Loop", false);

    private val okHttpClient = OkHttpClient()

//    constructor() {
//        timer.schedule(1000, minRequestTimeDelta) {
//            try {
//                if(queue.size > 0) {
//                    var request = queue.poll();
//                    Log.d("Web", "Request: ${request.getURI()}");
//                    val url = URL(request.getURI());
//                    val response = url.readText();
//                    Log.d("Web", "Response: $response");
//                    request.run(response);
//                } else {
//                    Log.d("Info", "nothing to send!");
//                }
//            } catch (e : Exception) {
//                Log.d("Web", "An exception occurred while executing web request: ${e.message}");
//            }
//        }
//    }

    constructor() {
        timer.schedule(1000, minRequestTimeDelta) {
            try {
                if(queue.size > 0) {
                    var requestData = queue.poll();
                    Log.d("Web", "Request: ${requestData.getURI()}");

                    var request: Request = Request.Builder().url(requestData.getURI()).build()

                    val response = okHttpClient.newCall(request).execute().use {
                        response -> requestData.run(response.body!!.string())
                    }

                    Log.d("Web", "Response: $response");
                } else {
                    Log.d("Info", "nothing to send!");
                }
            } catch (e : Exception) {
                Log.d("Web", "An exception occurred while executing web request: ${e.message}");
            }
        }
    }

    fun SendRequest(request : MarketRequest) {
        queue.add(request);
    }

    fun stop() {
        timer.cancel()
        timer.purge()
    }
}