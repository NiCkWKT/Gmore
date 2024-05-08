package edu.cuhk.csci3310.gmore


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.cuhk.csci3310.gmore.presentation.news.NewsViewModel
import edu.cuhk.csci3310.gmore.screens.BookmarkScreen
import edu.cuhk.csci3310.gmore.screens.Camera.CameraScreen
import edu.cuhk.csci3310.gmore.screens.Camera.LiveCameraScreen
import edu.cuhk.csci3310.gmore.screens.NewsScreen
import edu.cuhk.csci3310.gmore.screens.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController, newsViewModel: NewsViewModel) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.News.route
    ) {
        composable(route = ScreenRoute.News.route) {
            NewsScreen(newsViewModel)
        }
        composable(route = ScreenRoute.Profile.route) {
            ProfileScreen()
        }
        composable(route = ScreenRoute.Bookmark.route) {
            BookmarkScreen()
        }
        composable(route = ScreenRoute.Camera.route) {
            CameraScreen(navController)
        }
        composable(route = ScreenRoute.LiveCamera.route) {
            LiveCameraScreen()
        }
    }
}