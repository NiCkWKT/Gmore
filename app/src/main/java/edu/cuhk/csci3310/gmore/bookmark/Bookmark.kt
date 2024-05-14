package edu.cuhk.csci3310.gmore.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite

@Composable
fun BookmarkScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "BOOKMARK",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
@Preview
fun BookmarkScreenPreview() {
    BookmarkScreen()
}