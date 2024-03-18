package com.example.xpensemanager.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpensemanager.CommonDivider
import com.example.xpensemanager.Data.ExpenseCategories
import com.example.xpensemanager.Data.MonthlyBudgets
import com.example.xpensemanager.Data.TotalExpenseCategories
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.XMViewModel
import java.util.Calendar

@Composable
fun BudgetSettingScreen(navController: NavController, vm: XMViewModel) {

    val categories = TotalExpenseCategories
    var totalMonthlyBudgets = MonthlyBudgets(
        totalBudget = 0,
        housing = 0,
        utilities = 0,
        transportation = 0,
        groceries = 0,
        healthcare = 0,
        debtPayments = 0,
        restaurantBills = 0,
        entertainment = 0,
        education = 0,
        investments = 0,
        taxes = 0,
        clothing = 0,
        other = 0
    )
    val calendar = vm.selectedDate.value // Get the selected date from the MutableState

    val month = calendar.get(Calendar.MONTH) // Get the month (0-based, so January is 0)
    val year = calendar.get(Calendar.YEAR) // Get the year

// Create a formatted string representing the month and year
    val monthYear = "${month + 1}-${year}" // Add 1 to the month to make it 1-based

    val categoryValues by remember {
        mutableStateOf(
            mutableStateMapOf<String, Int>().apply {
                categories.forEach { categoryName ->
                    // Initialize each category value with the corresponding value from monthlyBudgetsMap
                    put(categoryName, vm.monthlyBudgetsMap[monthYear]?.let { monthlyBudgets ->
                        when (categoryName) {
                            "Total Budget" -> monthlyBudgets.totalBudget
                            "Housing" -> monthlyBudgets.housing
                            "Utilities" -> monthlyBudgets.utilities
                            "Transportation" -> monthlyBudgets.transportation
                            "Groceries" -> monthlyBudgets.groceries
                            "Healthcare" -> monthlyBudgets.healthcare
                            "Debt Payments" -> monthlyBudgets.debtPayments
                            "Restaurant Bills" -> monthlyBudgets.restaurantBills
                            "Entertainment" -> monthlyBudgets.entertainment
                            "Education" -> monthlyBudgets.education
                            "Investments" -> monthlyBudgets.investments
                            "Taxes" -> monthlyBudgets.taxes
                            "Clothing" -> monthlyBudgets.clothing
                            "Other" -> monthlyBudgets.other
                            else -> 0 // Handle other categories with default value 0
                        }
                    } ?: 0) // Default value is 0 if there's no corresponding monthly budget
                }
            }
        )
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),


        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                vm.isBudgetSettingScreenVisible = false
                navController.navigate(DestinationScreen.Budget.route)
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
            Text(text = "Set your Monthly Budget", fontWeight = FontWeight.Bold)
            Button(
                onClick = {
                    totalMonthlyBudgets = updateBudgetSettingsFromMap(categoryValues)
                    vm.updateBudgetValues(totalMonthlyBudgets)
                },

                ) {
                Text(
                    text = "Submit",
                    fontSize = 10.sp
                )
            }
        }

        CommonDivider()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(categories) { index, categoryName ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = categoryName, modifier = Modifier.weight(1f))

                    var value by remember { mutableStateOf(categoryValues[categoryName].toString()) }

                    TextField(
                        value = value,
                        onValueChange = {
                            value = it
                            categoryValues[categoryName] = it.toIntOrNull() ?: 0
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Perform action on keyboard done
                            }
                        ),
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
        }


    }

}

// Update budget settings with values from the categoryValues map
fun updateBudgetSettingsFromMap(categoryValues: Map<String, Int>): MonthlyBudgets {
    return MonthlyBudgets(
        totalBudget = categoryValues["Total Budget"] ?: 0,
        housing = categoryValues["Housing"] ?: 0,
        utilities = categoryValues["Utilities"] ?: 0,
        transportation = categoryValues["Transportation"] ?: 0,
        groceries = categoryValues["Groceries"] ?: 0,
        healthcare = categoryValues["Healthcare"] ?: 0,
        debtPayments = categoryValues["Debt Payments"] ?: 0,
        restaurantBills = categoryValues["Restaurant Bills"] ?: 0,
        entertainment = categoryValues["Entertainment"] ?: 0,
        education = categoryValues["Education"] ?: 0,
        investments = categoryValues["Investments"] ?: 0,
        taxes = categoryValues["Taxes"] ?: 0,
        clothing = categoryValues["Clothing"] ?: 0,
        other = categoryValues["Other"] ?: 0
    )
}