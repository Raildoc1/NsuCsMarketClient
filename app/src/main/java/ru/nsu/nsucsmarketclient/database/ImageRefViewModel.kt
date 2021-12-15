package ru.nsu.nsucsmarketclient.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageRefViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ImagesRepository

    init {
        val imagesDao = AppDatabase.getDatabase(application).imagesDao()
        repository = ImagesRepository(imagesDao)
    }

    fun addImageRef(imageRef: ImageRef) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addImageRef(imageRef)
        }
    }
}