package com.example.xpensemanager.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel

@Composable
fun StatsScreen (navController: NavController, vm: XMViewModel){
    BottomNavigationMenu(selectedItem = BottomNavigationItem.STATS, navController = navController)

}