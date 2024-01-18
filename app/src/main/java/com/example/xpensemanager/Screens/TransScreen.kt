package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.XMViewModel
@Composable

fun TransScreen (navController: NavController, vm: XMViewModel){
    Column{
        navController.navigate(DestinationScreen.Daily.route)
//        TransNavigationMenu(
//            selectedItem = TransNavigationItem.DAILY,
//            navController = navController)
//
//        BottomNavigationMenu(
//            selectedItem = BottomNavigationItem.TRANS,
//            navController = navController
//        )

    }


}