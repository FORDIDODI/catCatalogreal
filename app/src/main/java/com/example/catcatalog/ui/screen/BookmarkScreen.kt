package com.example.catcatalog.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.catcatalog.data.Cat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.tooling.preview.Preview

@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookmarkScreen() {
    val context = LocalContext.current
    var bookmarks by remember { mutableStateOf(loadBookmarks(context)) }
    var query by remember { mutableStateOf("") }
    val gson = remember { Gson() }

    fun removeBookmark(cat: Cat) {
        val prefs: SharedPreferences = context.getSharedPreferences("cat_bookmarks", Context.MODE_PRIVATE)
        val current = bookmarks.toMutableList()
        current.removeAll { it.id == cat.id }
        prefs.edit().putString("bookmarks", gson.toJson(current)).apply()
        bookmarks = current
    }

    val filteredBookmarks = if (query.isBlank()) bookmarks else bookmarks.filter {
        (it.breeds?.firstOrNull()?.name?.contains(query, ignoreCase = true) == true) ||
        it.id.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = query, onQueryChange = { query = it })
        Box(modifier = Modifier.weight(1f)) {
            if (filteredBookmarks.isEmpty()) {
                Text("No bookmarks yet.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredBookmarks) { cat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideImage(
                                    model = cat.url,
                                    contentDescription = "Cat Image",
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(100.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(cat.breeds?.firstOrNull()?.name ?: "ID: ${cat.id}")
                                    IconButton(onClick = { removeBookmark(cat) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Remove Bookmark")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun loadBookmarks(context: Context): List<Cat> {
    val prefs: SharedPreferences = context.getSharedPreferences("cat_bookmarks", Context.MODE_PRIVATE)
    val json = prefs.getString("bookmarks", null) ?: return emptyList()
    val type = object : TypeToken<List<Cat>>() {}.type
    return Gson().fromJson(json, type)
} 