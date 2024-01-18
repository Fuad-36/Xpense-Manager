package com.example.xpensemanager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.xpensemanager.DestinationScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.xpensemanager.navigateTo

//Daily, Calendar, Monthly, Notes
enum class TransNavigationItem(val title: String, val navDestination: DestinationScreen) {
    DAILY("Daily", DestinationScreen.Daily),
    CALENDAR("Calendar", DestinationScreen.Calendar),
    MONTHLY("Monthly", DestinationScreen.Monthly),
    NOTES("Notes", DestinationScreen.Notes)

}

@Composable
fun TransNavigationMenu(
    selectedItem: TransNavigationItem?,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color(0XFFDEEDFF)),
        verticalAlignment = Alignment.CenterVertically

    ) {
        for (item in TransNavigationItem.values()) {
            Text(
                text = item.title,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, item.navDestination.route)
                    }
                    .background(
                        color = if (item == selectedItem) Color.White else Color.Transparent
                    )
            )
        }
    }
}