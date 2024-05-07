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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.cuhk.csci3310.gmore.CameraActivity
import edu.cuhk.csci3310.gmore.ScreenRoute
import edu.cuhk.csci3310.gmore.ui.theme.OffWhite

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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop)

        }
    }
}


@Composable
@Preview
fun CameraScreenPreview() {
    val navController = rememberNavController()
    CameraScreen(navController)
}