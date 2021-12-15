package ru.nsu.nsucsmarketclient.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao() : ImagesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            val temp = INSTANCE
            if(temp != null) {
                return temp
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "images"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}