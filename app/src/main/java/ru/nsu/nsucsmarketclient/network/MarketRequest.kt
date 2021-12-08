package ru.nsu.nsucsmarketclient.network

class MarketRequest constructor(path: String, handler: (s : String) -> Unit) {
    private val handler = handler;
    private val path = path;

    fun getURI() : String {
        return "https://market.csgo.com/api/v2/$path"
    }

    fun run(response: String) {
        handler(response);
    }
}