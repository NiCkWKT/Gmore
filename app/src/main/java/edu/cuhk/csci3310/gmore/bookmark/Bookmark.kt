package edu.cuhk.csci3310.gmore.bookmark

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.data.db.MarkedNews
import edu.cuhk.csci3310.gmore.news_list.NewsListEvent
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite
import edu.cuhk.csci3310.gmore.util.UiEvent

@Composable
fun BookmarkScreen(
    onPopBackStack: () -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val markedNews = viewModel.markedNews.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    Scaffold(
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(markedNews.value) { news ->
//                    Text(text = news.title)
                    NewsCard(news, modifier = Modifier.padding(16.dp))
                }
            }
        }
    )

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(OffWhite),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "BOOKMARK",
//            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//    }
}

//@Composable
//@Preview
//fun BookmarkScreenPreview() {
//    BookmarkScreen()
//}

@Composable
fun NewsBookmarkRow(bookmark: MarkedNews) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imagerPainter = rememberAsyncImagePainter(model = Uri.parse(bookmark.imageUrl))
        Image(
            painter = imagerPainter,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = bookmark.title,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bookmark.summary1,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun NewsCard(
    newsData: MarkedNews,
    modifier: Modifier = Modifier
) {
    val imagerPainter = rememberAsyncImagePainter(model = Uri.parse(newsData.imageUrl))

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

                Button(
                    onClick = { /*TODO*/ }, // TODO: Navigate to the news detail
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(text = "More >", color = Color.Gray)
                }
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

                val point1 = newsData.summary1
                val point2 = newsData.summary2
                val point3 = newsData.summary3

                val summary = listOf<String>(point1, point2, point3)

                NewsSummaryView(summary)
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
