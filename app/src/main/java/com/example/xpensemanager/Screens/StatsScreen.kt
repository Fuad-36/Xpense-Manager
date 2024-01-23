package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel

@Composable
fun StatsScreen (navController: NavController, vm: XMViewModel){
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column (modifier = Modifier.padding(it)){


                Text(text = "Stats views ")
                // Add the rest of your content components here
            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.STATS,
                navController = navController
            )
        },

    )
}