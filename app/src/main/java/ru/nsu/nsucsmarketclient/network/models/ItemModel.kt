package ru.nsu.nsucsmarketclient.network.models

data class ItemModel (
    val item_id: String,

    val assetid: String,
    val classid: String,
    val instanceid: String,

    val real_instance: String,

    val market_hash_name: String,

    val position: Long,
    val price: Long,
    val currency: String,
    val status: String,

    val live_time: Long,

    val left: Long? = null,
    val botid: String
)