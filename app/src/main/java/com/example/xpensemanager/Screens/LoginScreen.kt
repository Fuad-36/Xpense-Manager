package com.example.xpensemanager.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.xpensemanager.CheckSignedIn
import com.example.xpensemanager.CommonProcessBar
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.XMViewModel
import com.example.xpensemanager.navigateTo

@Composable
fun LoginScreen(navController: NavHostController, vm: XMViewModel) {

    CheckSignedIn(vm = vm, navController = navController)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current

            Text(
                text = "Log In",
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5B67CA),
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(8.dp)

            )

            OutlinedTextField(
                value = emailState.value, onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = passwordState.value, onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Password (Minimum 6 characters)") },
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = { vm.logIn(emailState.value.text, passwordState.value.text) },
                modifier = Modifier
                    .padding(top = 25.dp)
                    .padding(8.dp)
            ) {
                Text(text = " LOG IN ")
            }

            Text(text = " Doesn't have any account? Sign Up",
                color = Color(0xFF2C406E),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.route)
                    }
            )


        }
    }
    if (vm.inProcess.value) {
        CommonProcessBar()
    }
}