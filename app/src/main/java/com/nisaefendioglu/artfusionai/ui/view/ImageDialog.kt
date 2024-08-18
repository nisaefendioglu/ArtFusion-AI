package com.nisaefendioglu.artfusionai.ui.view

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.nisaefendioglu.artfusionai.ui.theme.RedPrimary
import com.nisaefendioglu.artfusionai.ui.theme.BluePrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .background(RedPrimary, shape = RoundedCornerShape(12.dp))
                    .padding(1.dp)
            ) {
                Text("Close")
            }
        },
        text = {
            val painter = rememberAsyncImagePainter(model = imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(1.dp),
                contentScale = ContentScale.Fit
            )
        },
        dismissButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        downloadImage(imageUrl, context)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .background(BluePrimary, shape = RoundedCornerShape(12.dp))
                    .padding(1.dp)
            ) {
                Text("Download")
            }
        }
    )
}

suspend fun downloadImage(imageUrl: String, context: Context) {
    val imageLoader = coil.ImageLoader.Builder(context).build()
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(Size.ORIGINAL)
        .build()

    val result = imageLoader.execute(request)

    if (result.drawable is BitmapDrawable) {
        val bitmap = (result.drawable as BitmapDrawable).bitmap
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Downloaded_Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            withContext(Dispatchers.IO) {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
