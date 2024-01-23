package com.example.xpensemanager.Data

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.xpensemanager.DestinationScreen
import java.util.Calendar
import com.example.xpensemanager.R
import androidx.compose.ui.res.imageResource
import androidx.compose.foundation.Image


data class UserData(
    var userId: String?="",
    var name: String?="",
    var email: String?=""
){
    fun toMap()= mapOf(
        "userId" to userId,
        "name" to name,
        "email" to email
    )
}
data class Transaction(
    val id:String,
    val type: TransactionType,
    val category: String,
    val amount: Double,
    val date: Calendar

)
enum class TransactionType{
    Income,
    Expense
}
fun Transaction.toMap(): Map<String, Any> {
    val typeString = when (type) {
        TransactionType.Income -> "Income"
        TransactionType.Expense -> "Expense"
    }



    return mapOf(
        "id" to id,
        "type" to typeString,
        "category" to category,
        "amount" to amount,
        "date" to date
    )
}