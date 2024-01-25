package com.example.smartmirror.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartmirror.CustomObjects.CustomFace
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.concurrent.Executor

class AppViewModel : ViewModel() {
    // -- Parameters --

    lateinit var context: Context

    // Parameter to check if face is registered for face recognition
    var isRegistered: Boolean by mutableStateOf(false)

    // Parameter to check if the face recognition is verified
    var isVerified: Boolean by mutableStateOf(false)

    // Flag for the camera taking photo
    val capturingImage = MutableStateFlow(false)

    // Flag to return to last page
    val returnLastPage = MutableStateFlow(false)

    // Flag to image analyzing is done
    val isAnalyzing = MutableStateFlow(true)

    // Parameter to save person's name
    var personName: String by mutableStateOf("")

    // Parameter to show alert dialog on register
    var showAlertRegister: Boolean by mutableStateOf(false)

    // If there's a face registered then read and save its content
    lateinit var faceRegistered: CustomFace

    // Create FaceDetector
    private val cameraOptions =
        FaceDetectorOptions.Builder().setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f).enableTracking().build()

    val detector = FaceDetection.getClient(cameraOptions)

    lateinit var face: Face

    // Create camera executor
    lateinit var cameraExecutor: Executor

    // -- Functions --

    fun setCapturingImage(value: Boolean) {
        this.capturingImage.value = value
    }

    fun setReturnLastPage(value: Boolean) {
        this.returnLastPage.value = value
    }

    fun setIsAnalyzing(value: Boolean) {
        this.isAnalyzing.value = value
    }

    fun toFileString(face: Face, name: String): String {
        var finalString = "name_$name;"

        // Get boundingbox values
        val bounds = face.boundingBox
        finalString += "topLeftY_${bounds.top};" +
                "topLeftX_${bounds.left};" +
                "width_${bounds.width()};" +
                "height_${bounds.height()};"

        // Get rotation values
        val rotY = face.headEulerAngleY
        val rotX = face.headEulerAngleX
        val rotZ = face.headEulerAngleZ
        finalString += "rotY_${rotY};" +
                "rotX_${rotX};" +
                "rotZ_${rotZ};"

        // Get landmarks
        for (landmark in face.allLandmarks) {
            val type = landmark.landmarkType
            val position = landmark.position

            finalString += "$type _$position;"
        }

        return finalString
    }

    private fun openFile(): String {
        var fileContent = ""

        val filePath = "${this.context.getFilesDir().getPath()}/saved_face.txt"

        var file = File(filePath)

        if (file.exists()) {
            try {
                BufferedReader(FileReader(file)).use { reader ->
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {

                        fileContent += line
                    }
                }

            } catch (e: Exception) {
                println("File does not exist: $e")
            }
        }

        return fileContent
    }

    fun checkRegister() {
        val facePath = "${context.getFilesDir().getPath()}/saved_face.txt"

        if (File(facePath).exists()) {
            isRegistered = true

            this.faceRegistered = CustomFace(openFile())
            this.personName = this.faceRegistered.name
        }
    }
}