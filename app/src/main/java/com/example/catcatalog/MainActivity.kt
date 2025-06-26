package com.example.catcatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.catcatalog.ui.theme.CatCatalogTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.catcatalog.data.Cat
import com.example.catcatalog.data.CatRepository
import com.example.catcatalog.ui.AppNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.ExperimentalMaterial3Api

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatCatalogTheme {
                var catList by remember { mutableStateOf<List<Cat>>(emptyList()) }
                var isLoading by remember { mutableStateOf(true) }
                var error by remember { mutableStateOf<String?>(null) }
                val repository = remember { CatRepository() }
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    isLoading = true
                    error = null
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = repository.getCats()
                        result.onSuccess {
                            catList = it
                            isLoading = false
                        }.onFailure {
                            error = it.localizedMessage ?: "Unknown error"
                            isLoading = false
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Cat Catalog") },
                            actions = {
                                IconButton(onClick = { navController.navigate("about") }) {
                                    Icon(Icons.Filled.Info, contentDescription = "About")
                                }
                                IconButton(onClick = { navController.navigate("bookmarks") }) {
                                    Icon(Icons.Filled.Bookmark, contentDescription = "Bookmarks")
                                }
                                IconButton(onClick = { navController.navigate("settings") }) {
                                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    AppNavigation(
                        repository = repository,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatCatalogTheme {
        Greeting("Android")
    }
}