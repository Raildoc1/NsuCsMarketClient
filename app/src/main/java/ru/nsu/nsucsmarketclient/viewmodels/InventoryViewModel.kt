package ru.nsu.nsucsmarketclient.viewmodels

import android.util.Log
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val connection: MarketConnectionHandler,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private lateinit var inventory: List<InventoryItemModel>

    private var inventoryRefreshedCallback: (List<InventoryItemModel>) -> Unit = { }

    init {
        connection.setOnInventoryReceivedListener { l -> inventory = l; onInventoryRefreshed(l); Log.d("Info", "On received Inventory!"); }
    }

    private fun onInventoryRefreshed(l : List<InventoryItemModel>) {
        inventoryRefreshedCallback(l)
    }

    fun setInventoryRefreshedCallback(action : (List<InventoryItemModel>) -> Unit) {
        inventoryRefreshedCallback = action
    }

    fun forceUpdate() {
        forceUpdateShowcase()
        forceUpdateInventory()
    }

    fun forceUpdateShowcase() {
        connection.updateSaleItems()
    }

    fun forceUpdateInventory() {
        connection.updateInventoryItems()
    }

    fun addToSale(id: String, price: Long) {
        connection.addToSale(id, price)
    }

    fun setWebErrorMessageHandler(action : (String) -> Unit) {
        connection.onErrorMessage = action
    }

    fun updateItemsUrls(items : List<InventoryItemModel>) {
        for (i in items) {
            try {
                val ref = imagesDao.findByName("${i.classid}_${i.instanceid}")
                i.url = "https://steamcommunity-a.akamaihd.net/economy/image/${ref.ref}"
            } catch (e : Exception) {
                i.url = "none"
            }
        }
    }
}