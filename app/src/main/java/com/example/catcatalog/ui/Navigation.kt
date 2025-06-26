package com.example.catcatalog.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catcatalog.data.CatRepository
import com.example.catcatalog.ui.screen.AboutScreen
import com.example.catcatalog.ui.screen.CatDetailScreen
import com.example.catcatalog.ui.screen.CatListScreen
import android.net.Uri
import com.google.gson.Gson
import com.example.catcatalog.data.Cat
import com.example.catcatalog.ui.screen.BookmarkScreen
import com.example.catcatalog.ui.screen.SettingsScreen
import androidx.navigation.NavHostController

@Composable
fun AppNavigation(repository: CatRepository, navController: NavHostController) {
    val gson = remember { Gson() }
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            CatListScreen(navController, repository)
        }
        composable(
            "detail/{catJson}",
            arguments = listOf(navArgument("catJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val catJson = backStackEntry.arguments?.getString("catJson") ?: ""
            val cat = try { gson.fromJson(Uri.decode(catJson), Cat::class.java) } catch (e: Exception) { null }
            CatDetailScreen(cat)
        }
        composable("about") {
            AboutScreen()
        }
        composable("bookmarks") {
            BookmarkScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
} 