package edu.cuhk.csci3310.gmore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.cuhk.csci3310.gmore.news_list.NewsViewModel
import edu.cuhk.csci3310.gmore.ui.theme.GmoreTheme

data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GmoreTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val newsViewModel = hiltViewModel<NewsViewModel>()
    val navItems = listOf(
        BottomNavigationItem(
            route = ScreenRoute.News.route,
            title = "News",
            selectedIcon = painterResource(id = R.drawable.filled_news_24),
            unselectedIcon = painterResource(id = R.drawable.outline_news_24)
        ),
//        BottomNavigationItem(
//            route = ScreenRoute.Profile.route,
//            title = "Profile",
//            selectedIcon = painterResource(id = R.drawable.filled_account_24),
//            unselectedIcon = painterResource(id = R.drawable.outline_account_24)
//        ),
        BottomNavigationItem(
            route = ScreenRoute.Bookmark.route,
            title = "Bookmark",
            selectedIcon = painterResource(id = R.drawable.filled_bookmark_24),
            unselectedIcon = painterResource(id = R.drawable.outline_bookmark_24)
        ),
        BottomNavigationItem(
            route = ScreenRoute.Camera.route,
            title = "Camera",
            selectedIcon = painterResource(id = R.drawable.filled_camera_24),
            unselectedIcon = painterResource(id = R.drawable.outline_camera_24)
        ),
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController, navItems = navItems)}
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                BottomNavGraph(navController = navController, newsViewModel = newsViewModel)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, navItems: List<BottomNavigationItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        navItems.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavigationItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            if(currentDestination?.hierarchy?.any {
                it.route == screen.route
            } != true) navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }},
        label = {
            Text(text = screen.title)
        },
        alwaysShowLabel = true,
        icon = {
            Icon(painter = if(currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true) {
                screen.selectedIcon
            } else screen.unselectedIcon,
                contentDescription = screen.title)
        })
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
    GmoreTheme {
        Greeting("Android")
    }
}