package com.nisaefendioglu.artfusionai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.nisaefendioglu.artfusionai.ui.theme.ArtFusionAITheme
import com.nisaefendioglu.artfusionai.ui.viewmodel.MainViewModel
import com.nisaefendioglu.artfusionai.ui.view.ImageGeneratorScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtFusionAITheme {
                ImageGeneratorScreen(viewModel)
            }
        }
    }
}