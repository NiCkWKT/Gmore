package edu.cuhk.csci3310.gmore


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.cuhk.csci3310.gmore.screens.BookmarkScreen
import edu.cuhk.csci3310.gmore.screens.CameraScreen
import edu.cuhk.csci3310.gmore.screens.NewsScreen
import edu.cuhk.csci3310.gmore.screens.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.News.route
    ) {
        composable(route = BottomBarScreen.News.route) {
            NewsScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        composable(route = BottomBarScreen.Bookmark.route) {
            BookmarkScreen()
        }
        composable(route = BottomBarScreen.Camera.route) {
            CameraScreen()
        }
    }
}