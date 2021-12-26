package ru.nsu.nsucsmarketclient.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.nsu.nsucsmarketclient.BuildConfig
import ru.nsu.nsucsmarketclient.R
import ru.nsu.nsucsmarketclient.network.MarketRequest
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import javax.inject.Inject

@HiltViewModel
class MarketItemsViewModel @Inject constructor() : ViewModel() {

    private lateinit var showcase: List<ItemModel>
    private lateinit var inventory: List<InventoryItemModel>

    @Inject
    lateinit var connection: MarketConnectionHandler

    private var showcaseRefreshedCallback: (List<ItemModel>) -> Unit = { }
    private var inventoryRefreshedCallback: (List<InventoryItemModel>) -> Unit = { }

    fun setConnectionListeners() {
        if(!this::connection.isInitialized) {
            return
        }
        connection.setOnItemsReceivedListener { l -> showcase = l; onShowcaseRefreshed(l); Log.d("Info", "On received showcase!"); }
        connection.setOnInventoryReceivedListener { l -> inventory = l; onInventoryRefreshed(l); Log.d("Info", "On received Inventory!"); }
    }

    private fun onShowcaseRefreshed(l : List<ItemModel>) {
        showcaseRefreshedCallback(l)
    }

    private fun onInventoryRefreshed(l : List<InventoryItemModel>) {
        inventoryRefreshedCallback(l)
    }

    fun setShowcaseRefreshedCallback(action : (List<ItemModel>) -> Unit) {
        showcaseRefreshedCallback = action
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
}