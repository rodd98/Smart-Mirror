package com.example.smartmirror.pages

import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
fun RegisterPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appViewModel: AppViewModel
) {

    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(modifier = Modifier.fillMaxSize()) {


        CameraPreviewRegister(
            modifier = modifier.fillMaxSize(),
            imageCapture = imageCapture,
            appViewModel = appViewModel
        )


        Box(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(30.dp)
                .height(100.dp)
        ) {
            Text(
                "Align your face with the face contour shown below",
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
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
                    if (returnLastPage.value) {
                        appViewModel.showAlertRegister = true
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
            CustomAlertDialog(
                isVisible = appViewModel.showAlertRegister,
                appViewModel = appViewModel,
                navController = navController
            )
        }

        appViewModel.isAnalyzing.collectAsState().let { isAnalyzing ->
            if (!isAnalyzing.value) {
                val customFace = CustomFace(
                    appViewModel.toFileString(
                        appViewModel.face,
                        appViewModel.personName
                    )
                )

                appViewModel.faceRegistered = customFace

                customFace.saveToFile(appViewModel.context)
            }
        }

    }
}

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    appViewModel: AppViewModel,
    navController: NavHostController
) {
    if (!isVisible) {
        return
    }

    var alertText = remember { mutableStateOf("Type your name") }

    Box(
        modifier = modifier
            .size(250.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(20.dp),
        contentAlignment = Alignment.Center

    ) {
        Column(
            modifier = modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                alertText.value, fontSize = 20.sp, textAlign = TextAlign.Center
            )

            Spacer(modifier = modifier.height(15.dp))

            OutlinedTextField(
                value = appViewModel.personName,
                onValueChange = { appViewModel.personName = it })

            Row(modifier = modifier.fillMaxHeight(), verticalAlignment = Alignment.Bottom) {

                Button(onClick = {
                    appViewModel.setReturnLastPage(false)
                    appViewModel.showAlertRegister = false
                }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "cancel")
                }

                Spacer(modifier = modifier.width(15.dp))

                Button(onClick = {
                    if (appViewModel.personName == "") {
                        alertText.value = "You must type your name!"
                    } else {

                        appViewModel.showAlertRegister = false

                        navController.navigate("start")
                        // appViewModel.checkFace()
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = "confirm")
                }
            }
        }
    }
}