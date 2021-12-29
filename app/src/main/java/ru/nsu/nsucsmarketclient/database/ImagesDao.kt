package ru.nsu.nsucsmarketclient.database

import androidx.room.*

@Dao
interface ImagesDao {
    @Query("SELECT * FROM images WHERE id LIKE :id LIMIT 1")
    fun findByName(id: String): ImageRef

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: ImageRef)

    @Delete
    fun delete(user: ImageRef)
}