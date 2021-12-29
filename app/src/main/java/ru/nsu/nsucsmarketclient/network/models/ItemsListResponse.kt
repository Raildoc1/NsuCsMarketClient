package ru.nsu.nsucsmarketclient.network.models

data class ItemsListResponse(
    val success: Boolean,
    val items: List<ItemModel>
)
