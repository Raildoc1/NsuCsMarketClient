package ru.nsu.nsucsmarketclient.database

import javax.inject.Inject

class ImagesRepository @Inject constructor (private val imagesDao: ImagesDao) {
    fun findByName(name : String) : ImageRef {
        return imagesDao.findByName(name)
    }

    fun addImageRef(image: ImageRef) {
        imagesDao.insertAll(image)
    }
}