package ru.nsu.nsucsmarketclient.network

interface IResponseHandler {
    fun execute(response : String);
}