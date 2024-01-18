package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel

@Composable
fun MonthlyScreen(navController: NavController, vm: XMViewModel) {

    Column {
        CommonTopBar(navController = navController, vm = vm)

        Text(text = "monthly views ")
        TransNavigationMenu(
            selectedItem = TransNavigationItem.MONTHLY,
            navController = navController
        )
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.TRANS,
            navController = navController
        )

    }

}