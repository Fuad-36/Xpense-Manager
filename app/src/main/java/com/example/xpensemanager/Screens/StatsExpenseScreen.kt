//expense
package com.example.xpensemanager.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpensemanager.Data.TransactionDetails
import com.example.xpensemanager.Data.TransactionType
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.XMViewModel
import com.example.xpensemanager.navigateTo
import java.util.Calendar

@Composable
fun StatsExpenseScreen(navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {

                // ExpenseStat( navController = navController)
                if (vm.isTransactionScreenVisible) {
                    navController.navigate(DestinationScreen.AddTransaction.route)
                }
                val transactionsList: State<Map<String, Map<String, MutableList<TransactionDetails>>>> =
                    vm.transactionsMap

                val date = vm.selectedDate.value
                val month = date.get(Calendar.MONTH) + 1
                val year = date.get(Calendar.YEAR)
                val keyString = "$month-$year"
                val innerMapKeys: List<String> =
                    transactionsList.value[keyString]?.keys?.toList() ?: emptyList()
                val idTransactionList : State<Map<String, Map<String, List<String>>>> = vm.IdTransactionsMap
                val innerIdTransMap = idTransactionList.value[keyString]
                val innerTransactionMap: Map<String, MutableList<TransactionDetails>> = transactionsList.value[keyString] ?: emptyMap()

                Row {
//                    val totalIncomeAndExpense by vm.calculateTotalIncomeAndExpense(innerTransactionMap)
//
//                    val totalIncome = totalIncomeAndExpense.first
//                    val totalExpense = totalIncomeAndExpense.second
                    val totalIncome = innerTransactionMap.values.flatten()
                        .filter { it.type == TransactionType.Income }
                        .sumByDouble { it.amount }

                    val totalExpense = innerTransactionMap.values.flatten()
                        .filter { it.type == TransactionType.Expense }
                        .sumByDouble { it.amount }

                    TotalIncomeExpenseR( navController = navController, totalIncome = totalIncome, totalExpense = totalExpense)
                }
                val incomeMap = mutableMapOf<String, Double>()
                val expenseMap = mutableMapOf<String, Double>()

                innerTransactionMap.values.forEach { transactionList ->
                    transactionList.forEach { transaction ->
                        val amount = transaction.amount
                        val category = transaction.category

                        // Determine whether the transaction is income or expense
                        when (transaction.type) {
                            TransactionType.Income -> {
                                incomeMap[category] = (incomeMap[category] ?: 0.0) + amount
                            }
                            TransactionType.Expense -> {
                                expenseMap[category] = (expenseMap[category] ?: 0.0) + amount
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(25.dp)
                ) {

//                    Surface(
//
//                        modifier = Modifier.weight(0.5f)
//                            .padding(10.dp)
//                    ) {
                    PieChar(
                        modifier = Modifier.weight(0.1f),
                        width = 200.dp,
                        height = 280.dp,
                        expenseMap = expenseMap
                    )
//                    }
                }
                ColorMapWithValue(expenseMap = expenseMap)

                // Add the rest of your content components here
            }
        },
        bottomBar = {
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.STATS,
                navController = navController
            )
        },
    )



}

@Composable
fun PieChar(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    expenseMap: Map<String, Double>
): List<Pair<String, Color>> { // Return list of pairs containing category name and color
    val legendData = mutableListOf<Pair<String, Color>>()

    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        var currentAngle = 0f
        val total = expenseMap.values.sum().toFloat()
        val colors = arrayOf(
            Color.Blue, Color.Red, Color.Green, Color.Magenta,
            Color.Yellow, Color.Cyan, Color.Gray, Color.Black,
            Color.DarkGray, Color.LightGray,
            Color(0XFFFFF2AF), Color(0XFFC9AFFF), Color(0XFFAFE6FD)
        )
        var index = 0
        expenseMap.forEach { (category, value) ->
            val sweepAngle = (value.toFloat() / total) * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = currentAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Fill,
            )
            drawArc(
                color = colors[index % colors.size],
                startAngle = currentAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                //style = Stroke(width = 10f)
                style = Stroke(width = 2.dp.toPx()),
            )
            currentAngle += sweepAngle
            index++
            legendData.add(Pair(category , colors[index % colors.size]))
        }
    }
    return legendData
}



@Composable
fun ColorMapWithValue(
    expenseMap: Map<String, Double>
) {
    val colors = arrayOf(
        Color.Blue, Color.Red, Color.Green, Color.Magenta,
        Color.Yellow, Color.Cyan, Color.Gray, Color.Black,
        Color.DarkGray, Color.LightGray,
        Color(0XFFFFF2AF), Color(0XFFC9AFFF), Color(0XFFAFE6FD)
    )
    val totalIncome = expenseMap.values.sum()

    LazyColumn {
        var index=0;
        items(expenseMap.entries.toList()) { (key, value) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = colors[index % colors.size])                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = key, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${String.format("%.2f", (value / totalIncome) * 100)}%")
            }
            index++
        }
    }
}

@Composable
fun TotalIncomeExpenseR(navController : NavController, totalIncome: Double, totalExpense: Double) {
    val difference = totalIncome - totalExpense

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            color = Color(0XFFAFE6FD),
            modifier = Modifier.weight(0.7f)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Income",
                    color = Color(0XFFFC0000),
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable {
                            navigateTo(navController, DestinationScreen.Statistics.route)
                        }
                )
                Text(
                    text = "$totalIncome",
                    color = Color(0XFF004EFC),
                    modifier = Modifier.alpha(0.8f),
                    fontSize = 13.sp
                )
            }
        }
        Surface(
            color = Color(0XFFFF9797),
            modifier = Modifier.weight(0.7f)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Expense",
                    color = Color(0xFF2C406E),
                    modifier = Modifier
                        .padding(6.dp)

                )
                Text(
                    text = "$totalExpense",
                    color = Color(0XFF004EFC),
                    modifier = Modifier.alpha(0.8f),
                    fontSize = 13.sp
                )
            }
        }

    }
}