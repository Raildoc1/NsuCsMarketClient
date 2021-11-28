package ru.nsu.nsucsmarketclient.network

import android.util.Log
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule

class MarketRequestQueue {
    private val minRequestTimeDelta : Long = 3000;

    private val queue: Queue<MarketRequest> = LinkedList();

    constructor() {
        Timer("Loop", false).schedule(1000, minRequestTimeDelta) {
            if(queue.size > 0) {
                var request = queue.poll();
                Log.d("Web", "Request: ${request.getURI()}");
                val url = URL(request.getURI());
                Log.d("Web", "Response: ${url.readText()}");
            } else {
                Log.d("Info", "nothing to send!");
            }
        }
    }

    fun SendRequest(request : MarketRequest) {
        queue.add(request);
    }
}