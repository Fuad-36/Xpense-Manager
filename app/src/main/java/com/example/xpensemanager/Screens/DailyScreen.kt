package com.example.xpensemanager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xpensemanager.Data.Transaction
import com.example.xpensemanager.Data.TransactionType
import com.example.xpensemanager.XMViewModel
import java.util.Calendar
import androidx.compose.runtime.collectAsState

import android.widget.DatePicker
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.example.xpensemanager.ColoredBackgroundText
import com.example.xpensemanager.Data.TransactionDetails
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.Taka
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun DailyScreen(navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {

                TransNavigationMenu(
                    selectedItem = TransNavigationItem.DAILY,
                    navController = navController
                )

                Text(text = "daily views ")
                if (vm.isTransactionScreenVisible) {
                    navController.navigate(DestinationScreen.AddTransaction.route)
                }
                val transactionsList: State<Map<String, Map<String, MutableList<TransactionDetails>>>> =
                    vm.transactionsMap

                val date = vm.selectedDate.value
                val month = date.get(Calendar.MONTH) + 1
                val year = date.get(Calendar.YEAR)
                val keyString = "$month-$year"
//                val innerMap : Map<String, MutableList<TransactionDetails>> = transactionsList[keyString]?: emptyMap()
                val innerMapKeys: List<String> =
                    transactionsList.value[keyString]?.keys?.toList() ?: emptyList()

                LazyColumn {
                    items(innerMapKeys) { innerMapkey ->
                        val dateString = "$keyString-$innerMapkey"
                        val transactionDetailsMap: Map<String, MutableList<TransactionDetails>> =
                            transactionsList.value[keyString] ?: emptyMap()

                        TransactionCard(
                            transactionDetailsMap = transactionDetailsMap,
                            dateString = dateString
                        )

                    }

                }

            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.TRANS,
                navController = navController
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vm.isTransactionScreenVisible = true
                },
                containerColor = Color(0XFF5B67CA),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        },
    )

}


@Composable
fun MenuAndDate(dayOfMonth: String, dayOfWeek: String, MonthYear: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                Icons.Default.MoreVert, contentDescription = null,
                modifier = Modifier.padding(1.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(140.dp) // Adjust the width of the DropdownMenu
        ) {
            DropdownMenuItem(
                text = { Text("Delete") }, // Use Text() to display text content
                onClick = { /*TODO*/ },
                modifier = Modifier.widthIn(140.dp)
            )
        }

        Text(
            text = dayOfMonth,
            fontWeight = FontWeight.Bold
        )

        ColoredBackgroundText(
            text = dayOfWeek,
            backgroundColor = Color(0XFFADB4ED),
            textColor = Color.White
        )
        Text(text = MonthYear, fontSize = 9.sp)
    }
}

//Map<String, MutableList<TransactionDetails>> map<"sun-15"<list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCard(
    transactionDetailsMap: Map<String, MutableList<TransactionDetails>>,
    dateString: String
) {


    //dateString = "1-2024-Mon-15"
    val components = dateString.split("-")

    val month = components[0].toInt()
    val year = components[1].toInt()
    val dayOfWeek = components[2]
    val dayOfMonth = components[3].toInt()
    val keyString = "$dayOfWeek-$dayOfMonth"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0XFFDEEDFF)),

                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuAndDate("$dayOfMonth", "$dayOfWeek", "$month.$year")

                var totalIncome = 0.0
                var totalExpense = 0.0
                val transList = transactionDetailsMap[keyString]
                if (transList != null) {
                    for (details in transList) {
                        when (details.type) {
                            TransactionType.Income -> totalIncome += details.amount
                            TransactionType.Expense -> totalExpense += details.amount
                        }
                    }
                }
                Taka(amount = totalIncome, color = Color.Blue)
                Taka(amount = totalExpense, color = Color.Red)

            }
            //
            val detailsList = transactionDetailsMap[keyString]

            if (detailsList != null) {
                Column {
                    detailsList.forEach { details ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Display category
                            Text(text = details.category)

                            // Display amount
                            if (details.type == TransactionType.Income)
                                Taka(amount = details.amount, color = Color.Blue)
                            else Taka(amount = details.amount, color = Color.Red)

                        }
                    }
                }
            } else {
                // Handle the case where detailsList is null (key not found)
            }

        }
    }
}


@Composable
fun Menu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                Icons.Default.MoreVert, contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.widthIn(140.dp) // Adjust the width of the DropdownMenu
        ) {
            DropdownMenuItem(
                text = { Text("Delete") }, // Use Text() to display text content
                onClick = { /*TODO*/ },
                modifier = Modifier.widthIn(140.dp)
            )
        }
    }
}

