package edu.cuhk.csci3310.gmore.news_list

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import edu.cuhk.csci3310.gmore.ScreenRoute
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.news_detail.NewsDetailScreen
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite
import edu.cuhk.csci3310.gmore.ui.theme.DarkBrown
import edu.cuhk.csci3310.gmore.util.UiEvent


data class TabItem(
    val title: String,
)

val tabItems = listOf(
    TabItem(title = "General"),
    TabItem(title = "Sports"),
    TabItem(title = "Entertainment"),
    TabItem(title = "Tech"),
    TabItem(title = "Business")
)


@Composable
fun NewsScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.News.route
    ) {
        composable(ScreenRoute.News.route) {
            NewsListPage(onNavigate = { navController.navigate(it.route) })
        }
        composable(
            route = ScreenRoute.NewsDetail.route + "?newsId={newsId}",
            arguments = listOf(
                navArgument(name = "newsId") {
                    type = NavType.StringType
                    defaultValue = "4699eb30-49e7-4133-9254-70a6b9e7774d"
                }
            )
        ) {
            NewsDetailScreen(onPopBackStack = { navController.popBackStack() })
        }
    }
}

@Composable
fun NewsListPage(
    onNavigate: (UiEvent.Navigate) -> Unit,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val uiState = newsViewModel.newsUiState
    val news by newsViewModel.news.collectAsState()

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = true) {
        newsViewModel.uiEvent.collect {event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = OffWhite,
                contentColor = Color.Black
            ) {
                tabItems.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                            newsViewModel.getNews(tabItem.title.lowercase())
                        },
                        text = {
                            if (index == selectedTabIndex) {
                                Text(
                                    text = tabItem.title,
                                    color = DarkBrown,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else {
                                Text(text = tabItem.title)
                            }
                        }
                    )
                }
            }

            LazyColumn {
                when (uiState) {
                    is NewsUiState.Loading -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Center)
                                    .padding(top = 10.dp)
                            )
                        }
                    }

                    is NewsUiState.Success -> {
                        items(news) { newsData: NewsData ->
//                            NewsImageCard(newsData = newsData)
                            NewsCard(
                                newsData = newsData,
                                onEvent = newsViewModel::onEvent,
                                modifier = Modifier
                                    .clickable {
                                        newsViewModel.onEvent(NewsListEvent.onNewsClick(newsData))
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }

                    is NewsUiState.Error -> {
                        item {
                            Text(text = "Cannot Connect to internet", color = Color.Black)
                            Text(text = "Please retry later", color = Color.Black)
                        }
                    }

                    is NewsUiState.Empty -> {

                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    newsData: NewsData,
    onEvent: (NewsListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val imagerPainter = rememberAsyncImagePainter(model = Uri.parse(newsData.image_url))

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier, // Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column{
            // News Image
            Image(painter = imagerPainter,
                contentDescription = "newsImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds,
            )

            // News source and "More" button
            Row( verticalAlignment = Alignment.CenterVertically){
                Text(text = "Source: ${newsData.source}",
                    modifier = Modifier.padding(start = 3.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Transparent))


                Text(text = "More >", color = Color.Gray)

            }

            // News Title
            Text(text = newsData.title,
                modifier = Modifier.padding(start = 3.dp),
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            )
            Column (
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .fillMaxHeight(),
                ) {

                NewsSummaryView(newsData.summary)
            }
        }
    }
}

@Composable
fun NewsSummaryView(newsDataSummary: List<String>) {
    Text(
        text = "★Smart View",
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = Color.Black,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
    )

    // Summary bullet points
    for (i in newsDataSummary.indices) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                Modifier
                    .size(5.dp)
                    .fillMaxHeight()
                    .background(Color.Transparent)
            )
            Canvas(
                modifier = Modifier
                    .size(10.dp)
            ) {
                drawCircle(Color.Black)
            }

            Spacer(
                Modifier
                    .size(10.dp)
                    .fillMaxHeight()
                    .background(Color.Transparent)
            )

            Text(
                text = newsDataSummary[i],
                color = Color.Black,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }

        if (i < newsDataSummary.size - 1) {
            Spacer(modifier = Modifier
                .padding(vertical = 3.dp)
                .background(Color.Transparent))
        }
    }
}

//@Preview
//@Composable
//fun NewsCardPreview() {
//    val sample = NewsData(
//         "4f6d4822-a8b5-44e4-95b8-096f1174b347",
//     "Best VPN for Netflix in 2024",
//     "2024-04-21T12:15:00.000000Z",
//    "https://www.cnet.com/a/img/resize/87f3c1d184710c78492dacfa8fd6cd98d2ad8ab0/hub/2024/02/02/6793dbef-dc32-478a-b984-10e3e10c6a20/netflix-streaming-logo-phone-6405.jpg?auto=webp&fit=crop&height=675&width=1200",
//    "cnet.com",
//    "https://www.cnet.com/tech/services-and-software/best-vpn-for-netflix/#ftag=CAD590a51e",
//     "tech",
//     listOf<String>(
//    "Consider the streaming services you want to unblock, and choose a VPN that supports those platforms.",
//    "Look for a VPN with a large server network, offering multiple servers in target countries to increase the chances of unblocking geo-restricted content.",
//    "Choose a VPN with fast speeds, strong privacy features, and device compatibility that meets your needs."
//     )
//    )
//
//    NewsCard(sample)
//}