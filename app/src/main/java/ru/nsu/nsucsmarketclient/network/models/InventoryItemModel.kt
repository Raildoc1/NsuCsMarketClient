package ru.nsu.nsucsmarketclient.network.models

data class InventoryItemModel (
    val id: String,
    val classid: String,
    val instanceid: String,
    val market_hash_name: String,
    val market_price: Float,
    val tradable: Long
)