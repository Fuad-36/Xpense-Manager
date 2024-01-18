package com.example.xpensemanager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthYearPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    confirmButtonCLicked: (Int, Int) -> Unit,
    cancelClicked: () -> Unit
) {

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

    var month by remember {
        mutableStateOf(months[currentMonth])
    }

    var year by remember {
        mutableStateOf(currentYear)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    if (visible) {

        AlertDialog(
            backgroundColor = Color.White,
            shape = RoundedCornerShape(10),
            title = {

            },
            text = {

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(90f)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year--
                                    }
                                ),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = year.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(-90f)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year++
                                    }
                                ),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )

                    }


                    Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        elevation = 0.dp
                    ) {

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisSpacing = 16.dp,
                            crossAxisSpacing = 16.dp,
                            mainAxisAlignment = MainAxisAlignment.Center,
                            crossAxisAlignment = FlowCrossAxisAlignment.Center
                        ) {

                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                month = it
                                            }
                                        )
                                        .background(
                                            color = Color.Transparent
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    val animatedSize by animateDpAsState(
                                        targetValue = if (month == it) 60.dp else 0.dp,
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing
                                        ), label = ""
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                color = if (month == it) Color(0XFF5B67CA) else Color.Transparent,
                                                shape = CircleShape
                                            )
                                    )

                                    Text(
                                        text = it,
                                        color = if (month == it) Color.White else Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )

                                }
                            }

                        }

                    }

                }

            },
            buttons = {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            cancelClicked()
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {

                        Text(
                            text = "Cancel",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                    }

                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            confirmButtonCLicked(
                                months.indexOf(month),
                                year
                            )
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = Color(0XFF5B67CA)),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {

                        Text(
                            text = "OK",
                            color = Color(0XFF5B67CA),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )

                    }

                }

            },
            onDismissRequest = {

            }
        )

    }

}
//@Composable
//fun MonthYearDisplay(navController: NavController, vm: XMViewModel) {
//    var visible by remember { mutableStateOf(false) }
//    val currentDate = vm.selectedDate.value
//    var currentMonth by remember {
//        mutableStateOf(currentDate.get(Calendar.MONTH))
//    }
//    var currentYear by remember {
//        mutableStateOf(currentDate.get(Calendar.YEAR))
//    }
//    val months = listOf(
//        "JAN",
//        "FEB",
//        "MAR",
//        "APR",
//        "MAY",
//        "JUN",
//        "JUL",
//        "AUG",
//        "SEP",
//        "OCT",
//        "NOV",
//        "DEC"
//    )
//    var date by remember {
//        mutableStateOf(
//            "${months[currentDate.get(Calendar.MONTH)]} ${
//                currentDate.get(
//                    Calendar.YEAR
//                )
//            }"
//        )
//    }
//    // Get the current navBackStackEntry
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//
//    // Get the current destination route
//    val currentDestination = navBackStackEntry?.destination?.route
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(0.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        IconButton(onClick = {
//            if (currentDate.get(Calendar.MONTH) == 0) {
//                vm.updateMonthYear(11, currentYear - 1)
//            } else {
//                vm.updateMonthYear(currentMonth - 1, currentYear)
//            }
////              Navigate to the current destination
//            currentDestination?.let { destination ->
//                navController.navigate(destination)
//            }
//        }) {
//            Icon(
//                painter = painterResource(id = R.drawable.previous),
//                contentDescription = null,
//                modifier = Modifier.size(20.dp).padding(0.dp)
//            )
//        }
//        Text(
//            text = date,
//            fontWeight = FontWeight.Bold,
//            color = Color(0XFF3864C3),
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .weight(1f)
//                .padding(0.dp)
//                .clickable {
//                    visible = true
//                }
//        )
//
//        MonthYearPicker(
//            visible = visible,
//            currentMonth = currentMonth,
//            currentYear = currentYear,
//            confirmButtonCLicked = { month_, year_ ->
//
//                visible = false
//                vm.updateMonthYear(month_, year_)
////              Navigate to the current destination
//                currentDestination?.let { destination ->
//                    navController.navigate(destination)
//                }
//            },
//            cancelClicked = {
//                visible = false
//            }
//        )
//
//        IconButton(onClick = {
//            if (currentDate.get(Calendar.MONTH) == 11) {
//                vm.updateMonthYear(0, currentYear + 1)
//            } else {
//                vm.updateMonthYear(currentMonth + 1, currentYear)
//            }
////              Navigate to the current destination
//            currentDestination?.let { destination ->
//                navController.navigate(destination)
//            }
//        }) {
//            Icon(
//                painter = painterResource(id = R.drawable.next),
//                contentDescription = null,
//                modifier = Modifier.size(20.dp).padding(0.dp)
//            )
//        }
//
//        DisposableEffect(currentDate) {
//            // This block will be executed when currentDate changes
//            onDispose {
//                date =
//                    "${months[currentDate.get(Calendar.MONTH)]} ${currentDate.get(Calendar.YEAR)}"
//            }
//        }
//
//
//    }
//
//}


