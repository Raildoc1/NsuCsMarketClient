package ru.nsu.nsucsmarketclient.network

import android.util.Log
import java.util.*
import kotlin.concurrent.schedule

class MarketRequestQueue {
    private val minRequestTimeDelta : Long = 300;

    private val queue: Queue<MarketRequest> = LinkedList<MarketRequest>();

    constructor() {
        Timer("Loop", false).schedule(1000, minRequestTimeDelta) {
            Log.d("test", "tick!");
        }
    }
}