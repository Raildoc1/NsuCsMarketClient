package ru.nsu.nsucsmarketclient.network

import android.util.Log
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule

class MarketRequestQueue {
    private val minRequestTimeDelta : Long = 1000;

    private val queue: Queue<MarketRequest> = LinkedList();

    constructor() {
        Timer("Loop", false).schedule(1000, minRequestTimeDelta) {
            if(queue.size > 0) {
                var request = queue.poll();
                Log.d("Web", "Request: ${request.getURI()}");
                val url = URL(request.getURI());
                val response = url.readText();
                Log.d("Web", "Response: $response");
                request.run(response);
            } else {
                Log.d("Info", "nothing to send!");
            }
        }
    }

    fun SendRequest(request : MarketRequest) {
        queue.add(request);
    }
}