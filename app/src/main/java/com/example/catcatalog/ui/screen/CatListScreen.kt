package com.example.catcatalog.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.catcatalog.data.Cat
import com.example.catcatalog.data.CatRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import android.content.Context
import android.content.SharedPreferences
import com.example.catcatalog.ui.screen.SearchBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CatListScreen(navController: NavController, repository: CatRepository) {
    val scope = rememberCoroutineScope()
    var cats by remember { mutableStateOf<List<Cat>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val gson = remember { Gson() }
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    var bookmarks by remember { mutableStateOf(loadBookmarks(context)) }

    fun isBookmarked(cat: Cat): Boolean = bookmarks.any { it.id == cat.id }
    fun toggleBookmark(cat: Cat) {
        val prefs: SharedPreferences = context.getSharedPreferences("cat_bookmarks", Context.MODE_PRIVATE)
        val current = loadBookmarks(context).toMutableList()
        if (isBookmarked(cat)) {
            current.removeAll { it.id == cat.id }
        } else {
            current.add(cat)
        }
        prefs.edit().putString("bookmarks", gson.toJson(current)).apply()
        bookmarks = current
    }

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        scope.launch {
            val result = repository.getCats()
            result.onSuccess {
                cats = it
                isLoading = false
            }.onFailure {
                error = it.localizedMessage ?: "Unknown error"
                isLoading = false
            }
        }
    }

    val filteredCats = if (query.isBlank()) cats else cats.filter {
        it.breeds?.firstOrNull()?.name?.contains(query, ignoreCase = true) == true
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = query, onQueryChange = { query = it })
        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = error ?: "Error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(filteredCats) { cat ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable(enabled = cat.url.isNotEmpty()) {
                                        val catJson = gson.toJson(cat)
                                        navController.navigate("detail/${Uri.encode(catJson)}")
                                    },
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
                                            .height(100.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(2f)) {
                                        Text(
                                            text = cat.breeds?.firstOrNull()?.name ?: "ID: ${cat.id}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        IconButton(onClick = { toggleBookmark(cat) }) {
                                            if (isBookmarked(cat)) {
                                                Icon(Icons.Filled.Bookmark, contentDescription = "Bookmarked")
                                            } else {
                                                Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Bookmark")
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
    }
} 