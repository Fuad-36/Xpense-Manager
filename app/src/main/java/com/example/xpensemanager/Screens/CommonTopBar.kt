package com.example.xpensemanager.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xpensemanager.XMViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.R
import java.util.Calendar

@Composable

fun CommonTopBar(navController: NavController, vm: XMViewModel) {
    var visible by remember { mutableStateOf(false) }
    val currentDate = vm.selectedDate.value
    var currentMonth by remember {
        mutableStateOf(currentDate.get(Calendar.MONTH))
    }
    var currentYear by remember {
        mutableStateOf(currentDate.get(Calendar.YEAR))
    }
    val months = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )
    var date by remember {
        mutableStateOf(
            "${months[currentDate.get(Calendar.MONTH)]} ${
                currentDate.get(
                    Calendar.YEAR
                )
            }"
        )
    }
    // Get the current navBackStackEntry
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Get the current destination route
    val currentDestination = navBackStackEntry?.destination?.route
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(Color(0XFFDEEDFF))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            IconButton(onClick = {
                if (currentDate.get(Calendar.MONTH) == 0) {
                    vm.updateMonthYear(11, currentYear - 1)
                } else {
                    vm.updateMonthYear(currentMonth - 1, currentYear)
                }
//              Navigate to the current destination
                currentDestination?.let { destination ->
                    navController.navigate(destination)
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.previous),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).alpha(0.5f),
                    tint = Color(0XFF3864C3)
                )
            }

            Text(
                text = date,
                fontWeight = FontWeight.Bold,
                color = Color(0XFF3864C3),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    visible = true
                }
            )
            MonthYearPicker(
                visible = visible,
                currentMonth = currentMonth,
                currentYear = currentYear,
                confirmButtonCLicked = { month_, year_ ->

                    visible = false
                    vm.updateMonthYear(month_, year_)
//              Navigate to the current destination
                    currentDestination?.let { destination ->
                        navController.navigate(destination)
                    }
                },
                cancelClicked = {
                    visible = false
                }
            )


            IconButton(onClick = {
                if (currentDate.get(Calendar.MONTH) == 11) {
                    vm.updateMonthYear(0, currentYear + 1)
                } else {
                    vm.updateMonthYear(currentMonth + 1, currentYear)
                }
//              Navigate to the current destination
                currentDestination?.let { destination ->
                    navController.navigate(destination)
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).alpha(0.5f),
                    tint = Color(0XFF3864C3)
                )
            }
        }

        IconButton(
            onClick = { navController.navigate(DestinationScreen.Profile.route) },
            modifier = Modifier.size(28.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.profileuser), contentDescription = null)
        }
    }
}
