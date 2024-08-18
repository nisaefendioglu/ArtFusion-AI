package com.nisaefendioglu.artfusionai.ui.view

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import pl.droidsonroids.gif.GifDrawable
import com.nisaefendioglu.artfusionai.R

@Composable
fun GifImage(
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                val gifDrawable = GifDrawable(context.resources, R.drawable.ai)
                setImageDrawable(gifDrawable)
            }
        },
        modifier = modifier
    )
}
