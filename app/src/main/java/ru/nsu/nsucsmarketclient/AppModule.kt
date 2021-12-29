package ru.nsu.nsucsmarketclient

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nsu.nsucsmarketclient.database.AppDatabase
import ru.nsu.nsucsmarketclient.database.ImageRef
import ru.nsu.nsucsmarketclient.database.ImagesDao
import ru.nsu.nsucsmarketclient.network.MarketConnectionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun createConnection() : MarketConnectionHandler {
        var connection = MarketConnectionHandler()
        connection.connect(BuildConfig.MCS_KEY)
        return connection
    }

    @Provides
    fun provideImagesDao(appDatabase: AppDatabase) : ImagesDao {
        return appDatabase.imagesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AppDatabase::class.java,
            "images"
        ).build()
    }
}