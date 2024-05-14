package edu.cuhk.csci3310.gmore.news_detail

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import edu.cuhk.csci3310.gmore.data.db.MarkedNews
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite
import edu.cuhk.csci3310.gmore.util.UiEvent



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    onPopBackStack: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    val uiState = viewModel.newsUiState
//    val news by viewModel.news.()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val imagePainter = rememberAsyncImagePainter(model = Uri.parse(viewModel.imageUrl))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite),
        contentAlignment = Alignment.TopCenter
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = "Detail",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Left,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        // TODO: Pop backstack
                        IconButton(onClick = {
                            viewModel.onEvent(NewsDetailEvent.onBackClick)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        // TODO: add to bookmark
                        val markedNews = MarkedNews(
                            id = viewModel.id,
                            title = viewModel.title,
                            imageUrl = viewModel.imageUrl,
                            source = viewModel.source,
                            sourceUrl = viewModel.sourceUrl,
                            publishedDate = viewModel.publishedDate,
                            summary1 = viewModel.point1,
                            summary2 = viewModel.point2,
                            summary3 = viewModel.point3
                        )
                        CustomCheckbox(
                            checked = markedNews.isSaved,
                            onCheckedChange = { isChecked ->
                                viewModel.onEvent(
                                    NewsDetailEvent.onSaveNewsClick(
                                        markedNews,
                                        isChecked
                                    )
                                )
                            }
                        )
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            when (uiState) {
                NewsDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(OffWhite),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text("Loading")
                    }
                }

                NewsDetailUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(OffWhite),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text("Error")
                    }
                }

                NewsDetailUiState.Success -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(OffWhite),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NewsContent(innerPadding, viewModel, imagePainter)
                    }
                }

                NewsDetailUiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(OffWhite),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text("Empty")
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun NewsContent(
    innerPadding: PaddingValues,
    viewModel: NewsDetailViewModel,
    imagePainter: AsyncImagePainter
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        Image(
            painter = imagePainter,
            contentDescription = "News Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "★Smart View",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                text = viewModel.point1,
                color = Color.Black,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }

        Spacer(modifier = Modifier
            .padding(vertical = 3.dp)
            .background(Color.Transparent))

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
                text = viewModel.point2,
                color = Color.Black,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }

        Spacer(modifier = Modifier
            .padding(vertical = 3.dp)
            .background(Color.Transparent))

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
                text = viewModel.point3,
                color = Color.Black,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = viewModel.source,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )

        Text(
            text = viewModel.publishedDate,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

//        Button(
//            onClick = {
//                      viewModel.onEvent(NewsDetailEvent.onSaveNewsClick(viewModel))
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Bookmark")
//        }
    }
}

//@Preview
//@Composable
//fun NewsDetailPage() {
//    NewsDetailScreen()
//}

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    IconButton(onClick = { onCheckedChange(!checked) }) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Localized description"
            )
        } else {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "Localized description"
            )
        }
    }
}