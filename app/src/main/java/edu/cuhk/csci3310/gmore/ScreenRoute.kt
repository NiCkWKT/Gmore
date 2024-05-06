package edu.cuhk.csci3310.gmore


sealed class ScreenRoute(
    val route: String,
) {
    object News : ScreenRoute(
        route = "news",
    )

    object Profile : ScreenRoute(
        route = "profile",
    )

    object Bookmark : ScreenRoute(
        route = "bookmark",
    )
    object Camera : ScreenRoute(
        route = "camera",
    )
    object LiveCamera : ScreenRoute(
        route = "liveCamera",
    )
}