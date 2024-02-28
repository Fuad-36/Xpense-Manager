package com.example.xpensemanager.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.xpensemanager.CommonDivider
import com.example.xpensemanager.Data.ExpenseCategories
import com.example.xpensemanager.Data.IncomeCategories
import com.example.xpensemanager.Data.Transaction
import com.example.xpensemanager.Data.TransactionType
import com.example.xpensemanager.DestinationScreen
import com.example.xpensemanager.ExpenseCategorySelection
import com.example.xpensemanager.IncomeCategorySelection
import com.example.xpensemanager.XMViewModel
import com.example.xpensemanager.navigateTo
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun AddTransactionsScreen(
    navController: NavController,
    vm: XMViewModel

) {
    var selectedType by remember {
        mutableStateOf(TransactionType.Income)
    }
    var selectedCategory by remember {
        mutableStateOf("")
    }
    var amount by remember {
        mutableStateOf("")
    }
    var selectedDate by remember {
        mutableStateOf(Calendar.getInstance())
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),


    ) {
        IconButton(onClick = {
            vm.isTransactionScreenVisible = false
            navController.navigate(DestinationScreen.Daily.route)
        }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransactionTypeButton(
                text = "Income",
                isSelected = selectedType == TransactionType.Income,
                onClick = { selectedType = TransactionType.Income }
            )
            TransactionTypeButton(
                text = "Expense",
                isSelected = selectedType == TransactionType.Expense,
                onClick = { selectedType = TransactionType.Expense }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        var pickedDate by remember {
            mutableStateOf(LocalDate.now())
        }

        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter
                    .ofPattern("MMM dd yyyy")
                    .format(pickedDate)
            }
        }
        val dateDialogState = rememberMaterialDialogState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text("Date       :     ")
            Text(
                text = "$formattedDate",
                modifier = Modifier.clickable(onClick = { dateDialogState.show() })
            )
        }
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {

                }
                negativeButton(text = "Cancel")
            },

            ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = Color(0XFF5B67CA),
                    dateActiveBackgroundColor = Color(0XFF5B67CA)
                )
            ) {
                pickedDate = it
            }

        }

        if(selectedType == TransactionType.Income){
            selectedCategory = IncomeCategories[0];
            IncomeCategorySelection{ category ->
                selectedCategory = category

            }

        }
        if(selectedType == TransactionType.Expense){
            selectedCategory = ExpenseCategories[0];
            ExpenseCategorySelection{ category ->
                selectedCategory = category

            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text("Amount  :")
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {

                val localdate = pickedDate
                val date = vm.convertLocalDateToDate(localdate)
                val transaction = Transaction(
                    type = selectedType,
                    category = selectedCategory,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    date = date
                )
                vm.onTransactionSave(transaction, context)
                navigateTo(navController, DestinationScreen.Daily.route)
            }) {
                Text("Save")
            }

            Button(onClick = {
                val localdate = pickedDate
                val date = vm.convertLocalDateToDate(localdate)
                val transaction = Transaction(
                    type = selectedType,
                    category = selectedCategory,
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    date = date
                )
                vm.onTransactionSave(transaction, context)

                selectedType = TransactionType.Income
                selectedCategory = ""
                amount = ""
                selectedDate = Calendar.getInstance()
            }) {
                Text("Continue")
            }
        }

    }

}


@Composable
fun TransactionTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .background(
                if (isSelected) Color.Gray else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            ),

        ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}
