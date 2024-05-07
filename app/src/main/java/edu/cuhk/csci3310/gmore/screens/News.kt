package edu.cuhk.csci3310.gmore.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import edu.cuhk.csci3310.gmore.presentation.news.NewsViewModel
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite

@Composable
fun NewsScreen(newsViewModel: NewsViewModel) {
    val newsViewModel = newsViewModel
    val state by newsViewModel.newsApiResult.observeAsState()

    LazyColumn {
        if (state?.data.isNullOrEmpty()){
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }

        state?.let {
            items(it.data){ newsData: NewsData ->
                NewsImageCard(newsData = newsData)
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