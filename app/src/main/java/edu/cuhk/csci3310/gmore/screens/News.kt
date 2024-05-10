package edu.cuhk.csci3310.gmore.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.presentation.news.NewsUiState
import edu.cuhk.csci3310.gmore.presentation.news.NewsViewModel
import edu.cuhk.csci3310.gmore.presentation.news.OcrUiState
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite


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
fun NewsScreen(newsViewModel: NewsViewModel) {
    val uiState = newsViewModel.newsUiState
    val news by newsViewModel.news.collectAsState()

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            tabItems.forEachIndexed { index, tabItem ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(text = tabItem.title)
                    }
                )
            }
        }

        LazyColumn {
            when(uiState) {
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
                    items(news){ newsData: NewsData ->
                        NewsImageCard(newsData = newsData)
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

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(OffWhite),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "NEWS",
//            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//    }
}

@Composable
fun NewsImageCard(newsData: NewsData) {
    val imagerPainter = rememberAsyncImagePainter(model = Uri.parse(newsData.image_url))

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(16.dp)
    ) {
        Box{
            Image(painter = imagerPainter,
                contentDescription = "newsImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds
            )

            Surface (
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    Text(text = "News Title: ${newsData.title}")
                    Text(text = "News Source: ${newsData.source}")
                }

            }
        }
    }

}

//@Composable
//@Preview
//fun NewsScreenPreview() {
//    NewsScreen()
//}