package com.example.xpensemanager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.xpensemanager.CommonDivider
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.R
import com.example.xpensemanager.XMViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: XMViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            IconButton(onClick = {
                navController.navigate(DestinationScreen.Transactions.route)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }

        }
        CommonDivider()
        Row(horizontalArrangement = Arrangement.Center) {

            Icon(
                painter = painterResource(id = R.drawable.profileuser),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
        }
        vm.userData.value?.name?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }
        vm.userData.value?.email?.let {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        }

        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = {
                    vm.logOut(context)
                    navController.navigate(DestinationScreen.LogIn.route)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))

            ) {
                Text(text = "LogOut", fontWeight = FontWeight.Bold)
            }
        }

    }
}