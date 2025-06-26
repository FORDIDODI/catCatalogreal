package com.example.catcatalog.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cat Catalog",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aplikasi katalog kucing yang menampilkan gambar dan informasi kucing dari TheCatApi. Dibuat dengan Jetpack Compose, Retrofit, dan Glide.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Powered by https://thecatapi.com/")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aplikasi ini dibuat oleh Shiddiq Tarigan", style = MaterialTheme.typography.bodySmall)
        }
    }
} 