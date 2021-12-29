package ru.nsu.nsucsmarketclient.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.network.models.InventoryListResponse
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import ru.nsu.nsucsmarketclient.network.models.ItemsListResponse
import java.lang.ref.WeakReference
import java.util.*
import kotlin.concurrent.schedule

class MarketConnectionHandler {
    private val pingTimeDelta: Long = 15000

    private var secretKey = ""
    private var queue: MarketRequestQueue = MarketRequestQueue()

    private lateinit var onItemsReceived: (List<ItemModel>) -> Unit
    private lateinit var onInventoryReceived: (List<InventoryItemModel>) -> Unit
    private lateinit var pingTimer: Timer

    lateinit var onError: WeakReference<(String) -> Unit>

    fun connect(secretKey: String) {
        this.secretKey = secretKey;
        startPing()
        updateSaleItems()
        updateInventoryItems()
    }

    fun startPing() {
        pingTimer = Timer("Ping", false)
        pingTimer.schedule(0, pingTimeDelta) {
            queue.sendRequest(
                MarketRequest(
                    "ping?key=$secretKey",
                    { s -> run { print(s) } },
                    { s -> onError.get()?.invoke(s) }
                )
            )
        }
    }

    fun addToSale(id: String, price: Long) {
        var priceStr = "${price * 100}"
        queue.sendRequest(
            MarketRequest(
                "add-to-sale?key=$secretKey&id=$id&price=$priceStr&cur=RUB",
                { },
                { s -> onError.get()?.invoke(s) })
        )
    }

    fun updateSaleItems() {
        queue.sendRequest(
            MarketRequest("items?key=$secretKey", { s ->
                run {
                    print(s)
                    if (::onItemsReceived.isInitialized) {
                        try {
                            val gson = Gson();
                            val response = gson.fromJson(s, ItemsListResponse::class.java)

                            Handler(Looper.getMainLooper()).post {
                                onItemsReceived(response.items)
                            }
                        } catch (e: Exception) {
                            Log.d(
                                "IO",
                                "An exception occurred while parsing a response: ${e.message}"
                            );
                        }
                    } else {
                        Log.d("Info", "Nobody cares about items :c");
                    }
                }
            }, { s -> onError.get()?.invoke(s) })
        )
    }

    fun updateInventoryItems() {
        queue.sendRequest(
            MarketRequest("my-inventory/?key=$secretKey", { s ->
                run {
                    print(s);
                    if (::onInventoryReceived.isInitialized) {
                        try {
                            val gson = Gson();
                            val response = gson.fromJson(s, InventoryListResponse::class.java)

                            Handler(Looper.getMainLooper()).post {
                                onInventoryReceived(response.items)
                            }
                        } catch (e: Exception) {
                            Log.d(
                                "IO",
                                "An exception occurred while parsing a response: ${e.message}"
                            )
                        }
                    } else {
                        Log.d("Info", "Nobody cares about inventory :c")
                    }
                }
            }, { s -> onError.get()?.invoke(s) })
        )
    }

    fun setOnItemsReceivedListener(action: (List<ItemModel>) -> Unit) {
        onItemsReceived = action
    }

    fun setOnInventoryReceivedListener(action: (List<InventoryItemModel>) -> Unit) {
        onInventoryReceived = action
    }

    fun stop() {
        pingTimer.cancel()
        pingTimer.purge()
        queue.stop()
    }
}