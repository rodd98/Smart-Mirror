package com.example.smartmirror.pages

import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartmirror.CustomObjects.CustomFace
import com.example.smartmirror.R
import com.example.smartmirror.Utils.captureImage
import com.example.smartmirror.viewmodel.AppViewModel

@Composable
fun SignInPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appViewModel: AppViewModel
) {

    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = modifier.fillMaxSize()) {

        CameraPreviewRegister(
            modifier = modifier.fillMaxSize(),
            imageCapture = imageCapture,
            appViewModel = appViewModel
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(30.dp)
                .height(100.dp)
        ) {
            Text(
                "Align your face with the face contour shown below",
                textAlign = TextAlign.Center, fontSize = 26.sp,
                modifier = modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center)

        ) {

            Image(
                painterResource(id = R.drawable.face_contour),
                contentDescription = null,
                modifier = modifier
                    .align(Alignment.Center)
                    .size(350.dp)
            )
        }

        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        )
        {
            Button(onClick = {
                captureImage(imageCapture, appViewModel, context)
            }, modifier = modifier.size(80.dp)) {

                appViewModel.capturingImage.collectAsState().let { capturingImage ->
                    if (capturingImage.value) {
                        CircularProgressIndicator(color = Color.Black)
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_camera_24px),
                            contentDescription = "camera icon"
                        )
                    }
                }

                appViewModel.returnLastPage.collectAsState().let { returnLastPage ->
                    if (returnLastPage.value && !appViewModel.isVerified) {
                        appViewModel.showAlertRegister = true
                    }
                }

                appViewModel.isAnalyzing.collectAsState().let { isAnalyzing ->
                    if (!isAnalyzing.value) {
                        val customFace2 = CustomFace(
                            appViewModel.toFileString(
                                appViewModel.face,
                                appViewModel.personName
                            )
                        )

                        try {
                            appViewModel.isVerified =
                                appViewModel.faceRegistered.compare(customFace2, 100f)
                        } catch (e: Exception) {
                            println("Error comparing two faces: $e")
                        }

                    }
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentAlignment = Alignment.Center

        ) {
            CustomAlertDialogSignIn(
                isVisible = appViewModel.showAlertRegister,
                appViewModel = appViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun CustomAlertDialogSignIn(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    appViewModel: AppViewModel,
    navController: NavHostController
) {
    if (!isVisible) {
        return
    }

    Box(
        modifier = modifier
            .size(250.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(20.dp),
        contentAlignment = Alignment.Center

    ) {

        Column(modifier = Modifier.align(Alignment.Center)) {
            if (appViewModel.isVerified) {
                Text(text = "${appViewModel.personName} verified!", fontSize = 20.sp)
            } else {
                Text(text = "Error verifying!\nPlease try again.", fontSize = 20.sp)
            }

            Spacer(modifier = modifier.height(15.dp))

            Button(
                onClick = { navController.navigate("start") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "OK")
            }
        }

    }
}