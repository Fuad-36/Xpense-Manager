package com.example.xpensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpensemanager.Screens.AddTransactionsScreen
import com.example.xpensemanager.Screens.BottomNavigationMenu
import com.example.xpensemanager.Screens.BudgetScreen
import com.example.xpensemanager.Screens.CalendarScreen
import com.example.xpensemanager.Screens.DailyScreen
import com.example.xpensemanager.Screens.LoginScreen
import com.example.xpensemanager.Screens.MonthlyScreen
import com.example.xpensemanager.Screens.NotesScreen
import com.example.xpensemanager.Screens.ProfileScreen
import com.example.xpensemanager.Screens.SignUpScreen
import com.example.xpensemanager.Screens.StatsExpenseScreen
import com.example.xpensemanager.Screens.StatsIncomeScreen
import com.example.xpensemanager.Screens.StatsScreen
import com.example.xpensemanager.Screens.TransScreen
import com.example.xpensemanager.ui.theme.XpenseManagerTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route: String) {
    object SignUp : DestinationScreen("signup")
    object LogIn : DestinationScreen("login")
    object Daily : DestinationScreen("daily")
    object Monthly : DestinationScreen("monthly")
    object Calendar : DestinationScreen("calendar")
    object Notes : DestinationScreen("notes")
    object AddTransaction : DestinationScreen("addTransaction")

    object StatsIncome : DestinationScreen("statsIncome")
    object StatsExpense : DestinationScreen("statsExpense")
    object Transactions : DestinationScreen("trans")
    object Statistics : DestinationScreen("stats")
    object Budget : DestinationScreen("budget")
    object Profile : DestinationScreen("profile")


}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XpenseManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val navController = rememberNavController()
//                    var vm = hiltViewModel<XMViewModel>()
//                    MainNavGraph(navController, vm)
                    XMNavigation()
                }
            }
        }
    }

    @Composable
    fun XMNavigation() {
        val navController = rememberNavController()
        var vm = hiltViewModel<XMViewModel>()

        NavHost(
            navController = navController,
            startDestination = DestinationScreen.SignUp.route

        ) {
            composable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreen.LogIn.route) {
                LoginScreen(navController, vm)
            }
            composable(DestinationScreen.Transactions.route) {
                TransScreen(navController, vm)
            }
            composable(DestinationScreen.Statistics.route) {
                StatsScreen(navController, vm)
            }
            composable(DestinationScreen.Budget.route) {
                BudgetScreen(navController, vm)
            }
            composable(DestinationScreen.Daily.route) {
                DailyScreen(navController, vm)
            }
            composable(DestinationScreen.Monthly.route) {
                MonthlyScreen(navController, vm)
            }
            composable(DestinationScreen.Calendar.route) {
                CalendarScreen(navController, vm)
            }
            composable(DestinationScreen.Notes.route) {
                NotesScreen(navController, vm)
            }
            composable(DestinationScreen.StatsIncome.route) {
                StatsIncomeScreen(navController, vm)
            }
            composable(DestinationScreen.StatsExpense.route) {
                StatsExpenseScreen(navController, vm)
            }
            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController, vm)
            }
            composable(DestinationScreen.AddTransaction.route) {
                AddTransactionsScreen(navController, vm)
            }

        }


    }
//    // Transactions navigation graph
//    @Composable
//    fun TransactionsNavGraph(navController: NavHostController, vm: XMViewModel) {
//        Column {
//            Button(onClick = { navController.navigate(DestinationScreen.Daily.route) }) {
//                Text("Go to Daily Screen")
//            }
//
//            NavHost(
//                navController = navController,
//                startDestination = DestinationScreen.Daily.route
//            ) {
//                composable(DestinationScreen.Daily.route) {
//                    DailyScreen(navController, vm)
//                }
//                composable(DestinationScreen.Calendar.route) {
//                    CalendarScreen(navController, vm)
//                }
//                composable(DestinationScreen.Monthly.route) {
//                    MonthlyScreen(navController, vm)
//                }
//                composable(DestinationScreen.Notes.route) {
//                    NotesScreen(navController, vm)
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun StatisticsNavGraph(navController: NavHostController, vm: XMViewModel) {
//        NavHost(
//            navController = navController,
//            startDestination = DestinationScreen.StatsIncome.route
//        ) {
//            composable(DestinationScreen.StatsIncome.route) {
//                StatsIncomeScreen(navController, vm)
//            }
//            composable(DestinationScreen.StatsExpense.route) {
//                StatsExpenseScreen(navController, vm)
//            }
//        }
//    }
//
//    // Main navigation graph
//    @Composable
//    fun MainNavGraph(navController: NavHostController, vm: XMViewModel) {
//        NavHost(
//
//            navController = navController,
//            startDestination = DestinationScreen.SignUp.route
//        ) {
//            composable(DestinationScreen.SignUp.route) {
//                SignUpScreen(navController, vm)
//            }
//            composable(DestinationScreen.LogIn.route) {
//                LoginScreen(navController, vm)
//            }
//            composable(DestinationScreen.Transactions.route) {
//                TransactionsNavGraph(navController, vm)
//            }
//            composable(DestinationScreen.Statistics.route) {
//                StatisticsNavGraph(navController, vm)
//            }
//            composable(DestinationScreen.Budget.route) {
//                BudgetScreen(navController, vm)
//            }
//        }
//    }


}

