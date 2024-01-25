package com.example.smartmirror.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartmirror.viewmodel.AppViewModel

@Composable
fun MirrorPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    Box(modifier = modifier.fillMaxSize()) {
        CameraPreview(
            modifier = modifier.fillMaxSize(),
            appViewModel = appViewModel
        )
    }

    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = { navController.navigate("start") }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Arrow",
                modifier = modifier.size(48.dp)
            )
        }
    }
}