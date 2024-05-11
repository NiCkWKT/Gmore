package edu.cuhk.csci3310.gmore.screens.Camera

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
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
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
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
import androidx.compose.runtime.saveable.rememberSaveable
import edu.cuhk.csci3310.gmore.screens.NewsSummaryView

import java.io.File


@OptIn(ExperimentalPermissionsApi::class, ExperimentalStdlibApi::class)
@Composable
fun CameraScreen(navController: NavHostController) {
    val cameraPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.CAMERA
    )
    val context = LocalContext.current
    var selectedImageUri by rememberSaveable (key = "image_uri") {
        mutableStateOf<Uri?>(null)
    }
    val ocrViewModel = hiltViewModel<OcrViewModel>()
    val uiState = ocrViewModel.ocrUiState
    val ocrSummaries by ocrViewModel.ocrSummaries.collectAsState()

    val scrollState = rememberScrollState()

    var takenPhoto by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var imageSource by remember {
        mutableStateOf<ImageSource?>(null)
    }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if(!(uri == null || uri == Uri.EMPTY)) {
                selectedImageUri = uri
                imageSource = ImageSource.ImagePicker
            }
        }
    )

//    val launcher1 = rememberLauncherForActivityResult(contract =
//    ActivityResultContracts.GetContent()) { uri: Uri? ->
//        imageUri1 = uri
//    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            try {
                val intent = it.data
                val cameraPhotoUri = (intent?.getStringExtra("IMAGEURI"))
                if (URLUtil.isValidUrl(cameraPhotoUri)) {
                    selectedImageUri = Uri.parse(cameraPhotoUri);
                    imageSource = ImageSource.InternalStorage
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
//                        val file = File(context.cacheDir,context.contentResolver.openFileDescriptor(selectedImageUri!!, "r", null).toString())
//                        context.openFileInput("imageTaken.jpg")
                        val file = when(imageSource) {
                            is ImageSource.ImagePicker -> selectedImageUri!!.asFile(context)

                            is ImageSource.InternalStorage -> {
//                                val stream = context.openFileInput("imageTaken.jpg")
//                                val bytes = stream.readBytes()
//                                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                                File(context.cacheDir,context.contentResolver.openFileDescriptor(selectedImageUri!!, "r", null).toString())
                                File(context.getFilesDir(),"imageTaken.jpg")
                            }
                            null -> null
                        }
//                        val file = selectedImageUri!!.asFile(context)
                        ocrViewModel.getSummaries(file!!)
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
                        NewsSummaryView(newsDataSummary = ocrSummaries)
                    }

                    is OcrUiState.Error -> {
                        Text(text = "Server Error", color = Color.Black)
                    }

                    is OcrUiState.Empty -> { }
                }
            }
        }

    }
}


@Composable
@Preview
fun CameraScreenPreview() {
    val navController = rememberNavController()
    CameraScreen(navController)
}

@Suppress("DEPRECATION")
fun Uri.asFile(context: Context): File? {
    context.contentResolver
        .query(this, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        ?.use { cursor ->
            cursor.moveToFirst()
            val cursorData =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

            return if (cursorData == null) {
                returnCursorData(this, context)?.let { File(it) }
            } else {
                File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)))
            }
        }
    return null
}

sealed interface ImageSource {
    object InternalStorage: ImageSource
    object ImagePicker: ImageSource
}

@Suppress("DEPRECATION")
fun returnCursorData(uri: Uri?, context: Context): String? {

    if (DocumentsContract.isDocumentUri(context, uri)) {
        val wholeID = DocumentsContract.getDocumentId(uri)
        val splits = wholeID.split(":".toRegex()).toTypedArray()
        if (splits.size == 2) {
            val id = splits[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            val sel = MediaStore.Images.Media._ID + "=?"

            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )

            val columnIndex: Int? = cursor?.getColumnIndex(column[0])
            if (cursor?.moveToFirst() == true) {
                return columnIndex?.let { cursor.getString(it) }
            }
            cursor?.close()
        }
    } else {
        return uri?.path
    }
    return null
}