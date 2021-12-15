package ru.nsu.nsucsmarketclient.database

class ImagesRepository(private val imagesDao: ImagesDao) {
    fun findByName(name : String) : ImageRef {
        return imagesDao.findByName(name)
    }

    fun addImageRef(image: ImageRef) {
        imagesDao.insertAll(image)
    }
}