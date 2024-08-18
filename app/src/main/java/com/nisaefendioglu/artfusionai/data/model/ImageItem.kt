package com.nisaefendioglu.artfusionai.data.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun ImageItem(imageUrl: String, onClick: (String) -> Unit) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(160.dp)
            .padding(8.dp)
            .clickable { onClick(imageUrl) }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 1000f)
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentScale = ContentScale.Crop
    )
}
