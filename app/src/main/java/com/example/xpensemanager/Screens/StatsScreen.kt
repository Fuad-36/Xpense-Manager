//income stat
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
fun StatsScreen (navController: NavController, vm: XMViewModel) {
    Scaffold(
        topBar = {
            CommonTopBar(navController = navController, vm = vm)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {

                // IncomeStat( navController = navController)
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

                    TotalIncomeExpenseRo( navController = navController, totalIncome = totalIncome, totalExpense = totalExpense)
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
                /*incomeMap.entries.forEach { entry ->
                    Text("Key: ${entry.key}, Value: ${entry.value}")
                }

// Log the keys and values of expenseMap
                expenseMap.entries.forEach { entry ->
                    Text("Key: ${entry.key}, Value: ${entry.value}")
                }*/


                Row(
                    modifier = Modifier.padding(25.dp)
                ) {

//                    Surface(
//
//                        modifier = Modifier.weight(0.5f)
//                            .padding(10.dp)
//                    ) {
                    val legendData = PieChart(
                        modifier = Modifier.weight(0.1f),
                        width = 200.dp,
                        height = 280.dp,
                        incomeMap = incomeMap
                    )
//                    }
                }


                ColorMapWithValues(incomeMap = incomeMap)


                /*Text(
                    text = "Expense Screen",
                    color = Color.Black,
                    modifier = Modifier.alpha(0.8f),
                    fontSize = 13.sp
                )*/

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
fun PieChart(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    incomeMap: Map<String, Double>
): List<Pair<String, Color>> { // Return list of pairs containing category name and color
    val legendData = mutableListOf<Pair<String, Color>>()

    Canvas(
        modifier = modifier
            .width(width)
            .height(height)
    ) {
        var currentAngle = 0f
        val total = incomeMap.values.sum().toFloat()
        val colors = arrayOf(
            Color.Blue, Color.Red, Color.Green, Color.Magenta,
            Color.Yellow, Color.Cyan, Color.Gray, Color.Black
        )
        var index = 0
        incomeMap.forEach { (category, value) ->
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
fun Legend(legendData: List<Pair<String, Color>>) {
    Column {
        legendData.forEach { (category, color) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = category)
            }
        }
    }
}


@Composable
fun TotalIncomeExpenseRo(navController : NavController, totalIncome: Double, totalExpense: Double) {
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
                    color = Color(0xFF2C406E),
                    modifier = Modifier
                        .padding(6.dp)
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
                    color = Color(0XFFFC0000),
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable {
                            navigateTo(navController, DestinationScreen.StatsExpense.route)
                        }
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
@Composable
fun ColorMapWithValues(

    incomeMap: Map<String, Double>

) {
    val colors = arrayOf(
        Color.Blue, Color.Red, Color.Green, Color.Magenta,
        Color.Yellow, Color.Cyan, Color.Gray, Color.Black
    )
    val totalIncome = incomeMap.values.sum()
    Column{
        incomeMap.entries.forEachIndexed { index, entry ->
            val (key, value) = entry
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = colors[index % colors.size])
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$key", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${((value/totalIncome) *100).toInt() }%")
//                Text(text = "$totalIncome")
            }
        }
    }
}


