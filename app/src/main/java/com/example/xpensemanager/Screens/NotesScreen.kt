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
fun NotesScreen(navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column (modifier = Modifier.padding(it)){

                TransNavigationMenu(
                    selectedItem = TransNavigationItem.NOTES,
                    navController = navController
                )
                Text(text = "Notes views ")
                // Add the rest of your content components here
            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.TRANS,
                navController = navController
            )
        },

    )
}