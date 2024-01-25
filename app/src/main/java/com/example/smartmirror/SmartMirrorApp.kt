package com.example.smartmirror

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartmirror.pages.MirrorPage
import com.example.smartmirror.pages.RegisterPage
import com.example.smartmirror.pages.SignInPage
import com.example.smartmirror.viewmodel.AppViewModel

@Composable
fun SmartMirrorApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appViewModel: AppViewModel,
) {
    NavHost(navController = navController, startDestination = "start") {

        composable("start") {
            StartPage(
                appViewModel = appViewModel,
                navController = navController
            )
        }

        composable("register") {
            RegisterPage(navController = navController, appViewModel = appViewModel)
        }

        composable("signIn") {
            SignInPage(navController = navController, appViewModel = appViewModel)
        }

        composable("mirror") {
            MirrorPage(navController = navController, appViewModel = appViewModel)
        }

    }
}