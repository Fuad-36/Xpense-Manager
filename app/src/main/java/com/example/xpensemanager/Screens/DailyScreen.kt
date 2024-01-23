package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel


@Composable
fun DailyScreen(navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column (modifier = Modifier.padding(it)){

                TransNavigationMenu(
                    selectedItem = TransNavigationItem.DAILY,
                    navController = navController
                )
                Text(text = "daily views ")
                // Add the rest of your content components here
            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.TRANS,
                navController = navController
            )
        },
        floatingActionButton = {
            // Add your FAB here
            FABTrans()
        },
    )

}

@Composable
fun FABTrans(){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = Color(0XFF5B67CA)
        ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}