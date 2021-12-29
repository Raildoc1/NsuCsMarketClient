package ru.nsu.nsucsmarketclient.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler
import ru.nsu.nsucsmarketclient.network.models.InventoryItemModel
import ru.nsu.nsucsmarketclient.network.models.ItemModel
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(
    private val connection: MarketConnectionHandler,
    private val imagesDao: ImagesDao
) : ViewModel() {

    private lateinit var showcase: List<ItemModel>
    private var showcaseRefreshedCallback: (List<ItemModel>) -> Unit = { }

    init {
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

    fun setWebErrorMessageHandler(action : WeakReference<(String) -> Unit>) {
        connection.onError = action
    }

    fun updateItemsUrls(items : List<ItemModel>, onFinish: WeakReference<(List<ItemModel>) -> Unit>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in items) {
                try {
                    val ref = imagesDao.findByName("${i.classid}_${i.instanceid}")
                    i.url = "https://steamcommunity-a.akamaihd.net/economy/image/${ref.ref}"
                } catch (e : Exception) {
                    i.url = "none"
                }
            }
            withContext(Dispatchers.Main) {
                onFinish.get()?.invoke(items)
            }
        }
    }
}