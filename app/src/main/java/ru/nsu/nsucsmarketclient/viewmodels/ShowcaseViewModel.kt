package ru.nsu.nsucsmarketclient.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor() : ViewModel() {

    private lateinit var showcase: List<ItemModel>

    @Inject
    lateinit var connection: MarketConnectionHandler

    private var showcaseRefreshedCallback: (List<ItemModel>) -> Unit = { }

    fun setConnectionListeners() {
        if(!this::connection.isInitialized) {
            return
        }
        connection.setOnItemsReceivedListener { l -> showcase = l; onShowcaseRefreshed(l); Log.d("Info", "On received showcase!"); }
    }

    private fun onShowcaseRefreshed(l : List<ItemModel>) {
        showcaseRefreshedCallback(l)
    }

    fun setShowcaseRefreshedCallback(action : (List<ItemModel>) -> Unit) {
        showcaseRefreshedCallback = action
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

    fun setWebErrorMessageHandler(action : (String) -> Unit) {
        connection.onErrorMessage = action
    }
}