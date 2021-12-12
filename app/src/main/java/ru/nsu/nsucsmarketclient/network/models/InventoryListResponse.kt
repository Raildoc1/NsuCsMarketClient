package ru.nsu.nsucsmarketclient.network.models

data class InventoryListResponse(
    val success: Boolean,
    val items: List<InventoryItemModel>
)
