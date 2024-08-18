package com.nisaefendioglu.artfusionai.ui.view

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nisaefendioglu.artfusionai.data.model.ImageItem
import com.nisaefendioglu.artfusionai.ui.theme.DarkBlue
import com.nisaefendioglu.artfusionai.ui.theme.DarkGray
import com.nisaefendioglu.artfusionai.ui.theme.LightBlue
import com.nisaefendioglu.artfusionai.ui.viewmodel.MainViewModel

@Composable
fun ImageGeneratorScreen(viewModel: MainViewModel) {
    var question by remember { mutableStateOf("") }
    var isLoading by viewModel.isLoading
    var selectedImageUrl by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val animatedHeight by animateDpAsState(
        targetValue = if (expanded) 150.dp else 400.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (expanded) 0.5f else 1f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.9f), Color.White)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(LightBlue, DarkBlue),
                        ),
                        shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
                    )
                    .padding(16.dp)
                    .alpha(animatedAlpha)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ArtFusion AI",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 130.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = question,
                        onValueChange = { question = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp)),
                        placeholder = { Text("Enter image title") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (question.isNotBlank()) {
                                expanded = !expanded
                                viewModel.fetchImages(question)
                            } else {
                                Toast.makeText(
                                    viewModel.getApplication(),
                                    "Please enter image title",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(1.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkGray,
                            contentColor = Color.White
                        ),
                    ) {
                        Text("Generate")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp)
                        .alpha(animatedAlpha)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(count = viewModel.imageList.value.size) { index ->
                        ImageItem(
                            imageUrl = viewModel.imageList.value[index].imageUrl,
                            onClick = { selectedImageUrl = it }
                        )
                    }
                }
            }
        }

        selectedImageUrl?.let { imageUrl ->
            ImageDialog(imageUrl, onDismiss = { selectedImageUrl = null })
        }
    }
}
