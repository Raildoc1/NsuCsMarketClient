package ru.nsu.nsucsmarketclient.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageRef (
    @PrimaryKey val id : String,
    @ColumnInfo val ref : String?
    )