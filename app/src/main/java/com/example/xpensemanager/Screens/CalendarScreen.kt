package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel

@Composable
fun CalendarScreen(navController: NavController, vm: XMViewModel) {
    Column {
        CommonTopBar(navController = navController, vm = vm)
        Text(text = "notes views ")
        TransNavigationMenu(
            selectedItem = TransNavigationItem.CALENDAR,
            navController = navController
        )
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.TRANS,
            navController = navController
        )

    }

}