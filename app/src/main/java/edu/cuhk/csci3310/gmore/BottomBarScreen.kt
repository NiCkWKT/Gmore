package edu.cuhk.csci3310.gmore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource


sealed class BottomBarScreen(
    val route: String,
) {
    object News : BottomBarScreen(
        route = "news",
    )

    object Profile : BottomBarScreen(
        route = "profile",
    )

    object Bookmark : BottomBarScreen(
        route = "bookmark",
    )
    object Camera : BottomBarScreen(
        route = "camera",
    )
}