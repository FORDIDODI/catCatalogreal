package com.example.catcatalog.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    var isDarkMode by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark Mode")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text("Reset All Bookmarks")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aplikasi ini dibuat oleh Shiddiq Tarigan", style = MaterialTheme.typography.bodySmall)
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reset Bookmarks") },
            text = { Text("Yakin ingin menghapus semua bookmark?") },
            confirmButton = {
                Button(onClick = {
                    resetBookmarks(context)
                    showDialog = false
                }) { Text("Ya") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Batal") }
            }
        )
    }
}

fun resetBookmarks(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences("cat_bookmarks", Context.MODE_PRIVATE)
    prefs.edit().remove("bookmarks").apply()
} 