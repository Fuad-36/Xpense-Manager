package com.example.xpensemanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.sp
import com.example.xpensemanager.Data.ExpenseCategories
import com.example.xpensemanager.Data.IncomeCategories

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }

}

@Composable
fun CommonProcessBar() {

    Row(modifier = Modifier
        .alpha(0.5f)
        .background(Color.LightGray)
        .clickable(enabled = false) {}
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun CheckSignedIn(vm: XMViewModel, navController: NavHostController) {

    val alreadySignIn = remember { mutableStateOf(false) }
    val signIn = vm.signedIn.value
    if (signIn && !alreadySignIn.value) {
        alreadySignIn.value = true
        navController.navigate(DestinationScreen.Transactions.route) {
            popUpTo(0)
        }

    }
}

@Composable
fun Taka(amount: Double, modifier: Modifier = Modifier, color: Color = Color.Black) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "৳", fontSize = 12.sp, color = color)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$amount", color = color, fontSize = 13.sp)
    }
}
@Composable
fun ColoredBackgroundText(text: String, backgroundColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(0.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            modifier = Modifier.padding(1.dp)
        )
    }
}
@Composable
fun CommonDivider(){
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier.alpha(0.3f)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeCategorySelection(onCategorySelected: (String) -> Unit) {
    var categories = IncomeCategories
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    val  setSelectedCategory = remember { mutableStateOf(categories[0]) }


    Row {
        Text(
            text = "Category: ",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Text(text =selectedCategory,
            color = Color.Black,
            modifier = Modifier.clickable { setExpanded(!expanded) }.padding(8.dp)
            )
        Text(
            text = if (expanded) "▲" else "▼",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    selectedCategory  = category
                    setExpanded(false)
                    onCategorySelected(selectedCategory)
                }) {
                    Text(text = category)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCategorySelection(onCategorySelected: (String) -> Unit) {
    val categories = ExpenseCategories
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val (selectedCategory, setSelectedCategory) = remember { mutableStateOf(categories[0]) }

    Row {
        Text(
            text = "Category: ",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Text(text =selectedCategory,
            color = Color.Black,
            modifier = Modifier.clickable { setExpanded(!expanded) }.padding(8.dp)
        )
        Text(
            text = if (expanded) "▲" else "▼",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    setSelectedCategory(category)
                    setExpanded(false)
                    onCategorySelected(category)
                }) {
                    Text(text = category)
                }
            }
        }
    }
}