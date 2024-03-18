package com.example.xpensemanager.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpensemanager.CommonDivider
import com.example.xpensemanager.Data.ExpenseCategories
import com.example.xpensemanager.Data.TransactionType
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.XMViewModel
import com.example.xpensemanager.navigateTo
import java.util.Calendar

@Composable
fun BudgetScreen(navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {


                Text(text = "budget views ")

                val incomeMap = mutableMapOf<String, Double>()
                val expenseMap = mutableMapOf<String, Double>()
                val transactions = vm.transactions
                transactions.value.forEach { transaction ->
                    val category = transaction.category
                    val amount = transaction.amount

                    when (transaction.type) {
                        TransactionType.Income -> {
                            val currentAmount = incomeMap[category] ?: 0.0
                            incomeMap[category] = currentAmount + amount
                        }

                        TransactionType.Expense -> {
                            val currentAmount = expenseMap[category] ?: 0.0
                            expenseMap[category] = currentAmount + amount
                        }
                    }
                }

                val totalExpense = expenseMap.values.sum()
                val monthlyBudgetsMap = vm.monthlyBudgetsMap
                val calendar = vm.selectedDate.value // Get the selected date from the MutableState

                val month = calendar.get(Calendar.MONTH) // Get the month (0-based, so January is 0)
                val year = calendar.get(Calendar.YEAR) // Get the year
                // Create a formatted string representing the month and year
                val monthYear = "${month + 1}-${year}" // Add 1 to the month to make it 1-based


                val currentMonthsBudget = monthlyBudgetsMap[monthYear]

                val totalBudgetValue = currentMonthsBudget?.totalBudget
                if (totalBudgetValue != 0) {

                    if (totalBudgetValue != null) {
                        CategoryRow(
                            categoryName = "Total Budget", // Assuming the category name for total budget is fixed
                            budget = totalBudgetValue,
                            expense = totalExpense
                        )
                    }

                }

                if (currentMonthsBudget != null) {
                    LazyColumn {
                        ExpenseCategories.forEach { categoryName ->
                            val budgetValue = when (categoryName) {
                                "Housing" -> currentMonthsBudget.housing
                                "Utilities" -> currentMonthsBudget.utilities
                                "Transportation" -> currentMonthsBudget.transportation
                                "Groceries" -> currentMonthsBudget.groceries
                                "Healthcare" -> currentMonthsBudget.healthcare
                                "Debt Payments" -> currentMonthsBudget.debtPayments
                                "Restaurant Bills" -> currentMonthsBudget.restaurantBills
                                "Entertainment" -> currentMonthsBudget.entertainment
                                "Education" -> currentMonthsBudget.education
                                "Investments" -> currentMonthsBudget.investments
                                "Taxes" -> currentMonthsBudget.taxes
                                "Clothing" -> currentMonthsBudget.clothing
                                "Other" -> currentMonthsBudget.other
                                else -> 0 // Default value if category name not found
                            }

                            val expenseValue = expenseMap[categoryName] ?: 0.0

                            if (budgetValue != 0) {
                                item {
                                    CategoryRow(
                                        categoryName = categoryName,
                                        budget = budgetValue,
                                        expense = expenseValue
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Handle the case where currentMonthsBudget is null
                }


//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
//                    CustomProgressBar(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(10.dp))
//                            .height(20.dp),
//                        width = 300.dp,
//                        backgroundColor = Color.Gray,
//                        foregroundColor = Brush.horizontalGradient(
//                            listOf(
//                                Color(0xFF0075FF),
//                                Color(0xFF1A74DE)
//                            )
//                        ),
//                        percent = 75,
//                        isShownText = true
//                    )
//
//                }

            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.BUDGET,
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vm.isBudgetSettingScreenVisible = true
                    navController.navigate(DestinationScreen.BudgetSetting.route)
                },
                containerColor = Color(0XFF5B67CA),
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)

            }
        }

    )
}

@Composable
fun CustomProgressBar(
    modifier: Modifier,
    width: Dp,
    backgroundColor: Color,
    foregroundColor: Brush,
    percent: Int,
    isShownText: Boolean
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(width)
    ) {
        Box(
            modifier = modifier
                .background(
                    if (percent >= 100) Brush.horizontalGradient(
                        listOf(
                            Color(0xFFFC5E49),
                            Color(0xFFD84531)
                        )
                    ) else foregroundColor
                )
                .width(width * percent / 100)
        )

        if (isShownText) {
            Text(
                text = "$percent %",
                modifier = Modifier
                    .align(Alignment.Center),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CategoryRow(categoryName: String, budget: Int, expense: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .weight(0.32f)
        ) {
            Text(text = categoryName,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2)
            Text(text = "$budget")
        }
        Column(
            modifier = Modifier
                .padding(5.dp)
                .weight(0.68f)
        ) {
            Row {
                CustomProgressBar(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(20.dp),
                    width = 300.dp,
                    backgroundColor = Color.Gray,
                    foregroundColor = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF0075FF),
                            Color(0xFF1A74DE)
                        )
                    ),
                    percent = (expense / budget * 100).toInt(),
                    isShownText = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${expense.toInt()}")
                val after = budget - expense
                Text(text = "${after.toInt()}")
            }

        }
    }
    CommonDivider()
}