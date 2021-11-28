package ru.nsu.nsucsmarketclient.network

class MarketRequest constructor(path: String, action: Runnable) {
    val action = action;

    private val path = path;

    fun getURI() : String {
        return "https://market.csgo.com/api/v2/$path"
    }
}