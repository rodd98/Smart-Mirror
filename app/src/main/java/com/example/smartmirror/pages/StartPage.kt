package com.example.smartmirror

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartmirror.viewmodel.AppViewModel

@Composable
fun StartPage(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavController
) {

    // Set initially value to false to avoid infinite loop
    appViewModel.setReturnLastPage(false)
    appViewModel.showAlertRegister = false

    // Recheck the face registered
    appViewModel.checkRegister()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.weight(0.7f)) {
            Text(
                "Smart Mirror",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(id = R.drawable.mirror_icon),
                contentDescription = "Mirror Picture",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        // If no face registered then show warning message
        if (!appViewModel.isRegistered) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(0.5f)
            ) {

                Icon(
                    imageVector = Icons.TwoTone.Warning,
                    contentDescription = "warning sign",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    "No face registered.",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(0.5f)
            ) {

                Text(
                    "Welcome, ${appViewModel.personName}!",
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                )
            }
        }
    }

    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = {
            // If not registered navigate to face register page
            if (!appViewModel.isRegistered) {
                navController.navigate("register")
            } else if (appViewModel.isRegistered && appViewModel.isVerified) {
                navController.navigate("mirror")
            } else if (appViewModel.isRegistered && !appViewModel.isVerified) {
                navController.navigate("signIn")
            }
        }) {
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Forward Arrow",
                modifier = Modifier
                    .size(48.dp)
            )
        }
    }
}


