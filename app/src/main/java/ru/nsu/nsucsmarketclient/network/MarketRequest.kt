package ru.nsu.nsucsmarketclient.network

class MarketRequest constructor(path: String, onSuccess: (s : String) -> Unit, onError: (s : String) -> Unit) {
    private val onSuccess = onSuccess
    private val onError = onError
    private val path = path

    fun getURI() : String {
        return "https://market.csgo.com/api/v2/$path"
    }

    fun run(response: String) {
        onSuccess(response);
    }

    fun fail(message: String) {
        onError(message)
    }
}