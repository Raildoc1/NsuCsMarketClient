package ru.nsu.nsucsmarketclient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}