package com.example.smartmirror.Utils

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.smartmirror.viewmodel.AppViewModel
import com.google.mlkit.vision.common.InputImage
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


fun captureImage(imageCapture: ImageCapture, appViewModel: AppViewModel, context: Context) {
    appViewModel.setCapturingImage(true)
    val file = File.createTempFile("img", ".jpg")
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    imageCapture.takePicture(
        outputFileOptions,
        appViewModel.cameraExecutor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                var imageUri = outputFileResults.savedUri

                if (imageUri != null) {
                    analyzeImageAndSave(appViewModel, outputFileResults.savedUri, context)
                } else {
                    println("Error: Null Image Uri")
                }

                // Update state
                appViewModel.setCapturingImage(false)
                appViewModel.setReturnLastPage(true)

                if (!appViewModel.isRegistered) {
                    appViewModel.isRegistered = true
                }
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error $exception")
            }
        })
}

fun analyzeImageAndSave(appViewModel: AppViewModel, imageUri: Uri?, context: Context) {
    val image: InputImage

    try {
        image = InputImage.fromFilePath(context, imageUri!!)
        val result = appViewModel.detector.process(image).addOnSuccessListener { faces ->
            if (faces.isNotEmpty()) {
                appViewModel.face = faces[0]
                appViewModel.setIsAnalyzing(false)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

suspend fun Context.cameraProvider(appViewModel: AppViewModel): ProcessCameraProvider =
    suspendCoroutine {
        val listenableFuture = ProcessCameraProvider.getInstance(this)
        listenableFuture.addListener(
            {
                it.resume(listenableFuture.get())

            }, ContextCompat.getMainExecutor(this)
        )
    }