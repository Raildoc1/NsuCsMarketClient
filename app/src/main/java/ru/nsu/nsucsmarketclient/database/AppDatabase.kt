package ru.nsu.nsucsmarketclient.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao() : ImagesDao
}