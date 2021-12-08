package ru.nsu.nsucsmarketclient.network

import java.util.Timer
import kotlin.concurrent.schedule

class MarketConnectionHandler {
    // time in seconds
    private val pingTimeDelta : Long = 15000;

    private var isActive = false;
    private var secretKey = "";
    private var queue : MarketRequestQueue = MarketRequestQueue();

    fun connect(secretKey: String) {
        this.secretKey = secretKey;
        Timer("Ping", false).schedule(3000, pingTimeDelta) {
            queue.SendRequest(MarketRequest("ping?key=$secretKey") { print("Water fuck motherfucka suka blyat") })
        }
        Timer("GetItems", false).schedule(5000, 10000) {
            queue.SendRequest(MarketRequest("items?key=$secretKey") { s -> run { print(s) } })
        }
    }
}