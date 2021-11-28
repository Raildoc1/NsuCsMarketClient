package ru.nsu.nsucsmarketclient.network

class MarketConnectionHandler {
    // time in seconds
    private val pingTimeDelta = 20f;

    private var isActive = false;
    private var secretKey = "";
    private var queue : MarketRequestQueue = MarketRequestQueue();

    fun connect(secretKey: String) {
        this.secretKey = secretKey;
    }
}