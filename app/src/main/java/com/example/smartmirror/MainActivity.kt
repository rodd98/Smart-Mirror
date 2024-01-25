package com.example.smartmirror

import android.Manifest.permission.CAMERA
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.smartmirror.ui.theme.SmartMirrorTheme
import com.example.smartmirror.viewmodel.AppViewModel
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {

    private val appViewModel = AppViewModel()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Check if the permission was granted.
        if (!isGranted) {
            finishAndRemoveTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appViewModel.cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            SmartMirrorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Request the permission to use the camera.
                    permissionLauncher.launch(CAMERA)

                    val navController = rememberNavController()

                    // Send the context to the ViewModel for further utilization
                    appViewModel.context = LocalContext.current

                    // Verify if there is a file with a face already registered
                    appViewModel.checkRegister()

                    SmartMirrorApp(appViewModel = appViewModel, navController = navController)
                }
            }
        }
    }
}




