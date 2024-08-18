package com.nisaefendioglu.artfusionai.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nisaefendioglu.artfusionai.data.ApiService
import com.nisaefendioglu.artfusionai.data.model.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val imageList = mutableStateOf<List<ImageModel>>(emptyList())
    val isLoading = mutableStateOf(false)

    private val apiService = ApiService.create()

    fun fetchImages(query: String) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getImages(query)
                if (response.isSuccessful) {
                    val images = response.body()?.images?.map { ImageModel(it.srcSmall) }
                    if (images != null) {
                        imageList.value = images
                    } else {
                        handleError("No images found")
                    }
                } else {
                    handleError("Failed to fetch images")
                }
            } catch (e: IOException) {
                handleError("Network Error")
            } catch (e: HttpException) {
                handleError("HTTP Error")
            } catch (e: Exception) {
                handleError("Unknown Error")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun handleError(message: String) {
        showError(message)
    }

    private fun showError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }
}