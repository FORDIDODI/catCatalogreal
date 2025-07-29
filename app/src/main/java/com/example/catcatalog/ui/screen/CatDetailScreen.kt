package com.example.catcatalog.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.catcatalog.data.Cat
import com.example.catcatalog.data.Breed

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CatDetailScreen(cat: Cat?) {
    if (cat == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cat not found", color = MaterialTheme.colorScheme.error)
        }
        return
    }
    val breed: Breed? = cat.breeds?.firstOrNull()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = cat.url,
            contentDescription = "Cat Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = breed?.name ?: "ID: ${cat.id}",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = breed?.description ?: "No description available.\nID: ${cat.id}\nURL: ${cat.url}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        breed?.let {
            Text("Origin: ${it.origin ?: "-"}")
            Text("Temperament: ${it.temperament ?: "-"}")
            Text("Life Span: ${it.lifeSpan ?: "-"} years")
            Text("Weight: ${it.weight?.metric ?: "-"} kg / ${it.weight?.imperial ?: "-"} lbs")
            if (!it.wikipediaUrl.isNullOrEmpty()) {
                Text("Wikipedia: ${it.wikipediaUrl}", color = MaterialTheme.colorScheme.primary)
            }
        } ?: run {
            Spacer(modifier = Modifier.height(8.dp))
            Text("No breed info available for this cat.")
        }
    }
} 