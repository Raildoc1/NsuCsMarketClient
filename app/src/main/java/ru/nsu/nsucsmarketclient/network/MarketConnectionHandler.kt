package ru.nsu.nsucsmarketclient.network

import android.util.Log
import com.google.gson.Gson
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.network.models.ItemsListResponse
import java.util.Timer
import kotlin.concurrent.schedule

class MarketConnectionHandler {
    // time in seconds
    private val pingTimeDelta : Long = 15000;

    private var isActive = false;
    private var secretKey = "";
    private var queue : MarketRequestQueue = MarketRequestQueue();

    private lateinit var onItemsReceived : (List<ItemModel>) -> Unit;

    fun connect(secretKey: String) {
        this.secretKey = secretKey;
        Timer("Ping", false).schedule(3000, pingTimeDelta) {
            queue.SendRequest(MarketRequest("ping?key=$secretKey") { s -> run { print(s) } })
        }
        Timer("GetItems", false).schedule(5000, 10000) {
            queue.SendRequest(MarketRequest("items?key=$secretKey") { s -> run {
                print(s);
                if(::onItemsReceived.isInitialized)
                {
                    try {
                        var gson = Gson();
                        var response = gson.fromJson(s, ItemsListResponse::class.java)
                        onItemsReceived(response.items);
                    } catch (e : Exception) {
                        Log.d("IO", "An exception occurred while parsing a response: ${e.message}");
                    }
                } else {
                    Log.d("Info", "Nobody cares about items :c");
                }
            } })
        }
    }

    fun setOnItemsReceivedListener(action : (List<ItemModel>) -> Unit) {
        onItemsReceived = action;
    }
}