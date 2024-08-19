package com.nisaefendioglu.artfusionai.data.response

data class ImageResponse(val images: List<ImageData>)
data class ImageData(val srcSmall: String)