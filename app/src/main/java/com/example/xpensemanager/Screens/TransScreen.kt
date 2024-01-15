package com.example.xpensemanager.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel
@Composable

fun TransScreen (navController: NavController, vm: XMViewModel){
    BottomNavigationMenu(selectedItem = BottomNavigationItem.TRANS, navController = navController)
}