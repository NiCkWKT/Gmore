package edu.cuhk.csci3310.gmore.screens.Camera

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.webkit.URLUtil
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.cuhk.csci3310.gmore.CameraActivity
import edu.cuhk.csci3310.gmore.ScreenRoute
import edu.cuhk.csci3310.gmore.data.api.model.NewsData
import edu.cuhk.csci3310.gmore.presentation.news.OcrUiState
import edu.cuhk.csci3310.gmore.presentation.news.OcrViewModel
import edu.cuhk.csci3310.gmore.screens.NewsImageCard
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite
import java.io.File

@OptIn(ExperimentalPermissionsApi::class, ExperimentalStdlibApi::class)
@Composable
fun CameraScreen(navController: NavHostController) {
    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA
    )
    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val ocrViewModel = hiltViewModel<OcrViewModel>()
    val uiState = ocrViewModel.ocrUiState
    val ocrSummaries by ocrViewModel.ocrSummaries.collectAsState()

    val scrollState = rememberScrollState()

    var takenPhoto by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(!(uri == null || uri == Uri.EMPTY)) {
                selectedImageUri = uri
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            try {
                val intent = it.data
                val cameraPhotoUri = (intent?.getStringExtra("IMAGEURI"))
                if (URLUtil.isValidUrl(cameraPhotoUri)) {
                    selectedImageUri = Uri.parse(cameraPhotoUri);
                } else {
                    Log.e("mytag", "invalid uri");
                }
            } catch (e: Exception) {
                Log.e("mytag", e.toString());
            }

        } else {
            return@rememberLauncherForActivityResult
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite),
        contentAlignment = Alignment.Center
    ) {



        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedImageUri == null || selectedImageUri == Uri.EMPTY) {
                Text(
                    text = "OCR your text",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
//            Button(onClick = {
//                if (!cameraPermissionState.status.isGranted) {
//                    cameraPermissionState.launchPermissionRequest();
//                    Log.d("myTag", "Ask permission")
//                } else {
//                    navController.navigate(ScreenRoute.LiveCamera.route) {
//                        launchSingleTop = true
//                    }
//                }
//            }) {
//                Text(text = "Camera with Tab Bar")
//            }


                Button(onClick = {
                    if (!cameraPermissionState.status.isGranted) {
                        cameraPermissionState.launchPermissionRequest();
                    } else {
                        cameraLauncher.launch(Intent(context, CameraActivity::class.java))
                    }
                }) {
                    Text(text = "Take photo with Camera")
                }
                Button(onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )

                }) {
                    Text(text = "Select photo from album")
                }
            }




            if (!(selectedImageUri == null || selectedImageUri == Uri.EMPTY)) {
                IconButton(
                    onClick = {
                        selectedImageUri = null
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.Start)
                        .padding(top = 10.dp, start = 10.dp, bottom = 10.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Photo"
                    )
                }

                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = {
                        val cacheFile = File(selectedImageUri.toString())
                        ocrViewModel.getSummaries(cacheFile)
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(top = 10.dp, end = 10.dp, bottom = 10.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send image to OCR"
                    )
                }

                when(uiState) {
                    is OcrUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }

                    is OcrUiState.Success -> {
                        ocrSummaries.forEach { summary: String ->
                            Text(text = "Summary: ${summary}", color = Color.Black)
                        }
                    }

                    is OcrUiState.Error -> {
                        Text(text = "Server Error", color = Color.Black)
                    }

                    is OcrUiState.Empty -> { }
                }
            }

//            if (uiState == OcrUiState.Loading) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .wrapContentSize(align = Alignment.Center)
//                )
//            }
//            if (uiState.) {
//                ocrSummaries.forEach(
//                    summary: String ->
//                    Text(text = "Summary: ${summary}")
//                )
//
//            }

        }


    }
}


@Composable
@Preview
fun CameraScreenPreview() {
    val navController = rememberNavController()
    CameraScreen(navController)
}