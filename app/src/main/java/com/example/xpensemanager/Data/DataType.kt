package com.example.xpensemanager.Data

import androidx.annotation.Keep
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.UUID
import java.util.*


data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var email: String? = ""
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "email" to email
    )
}
@Keep
data class Transaction(
    val id: String = "",
    val type: TransactionType,
    val category: String,
    val amount: Double,
    val date: Date

) {
    constructor() : this("", TransactionType.Income, "", 0.0, Date())

    fun getMonthYearKey(): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        // Adding 1 to get the correct month (January is 0, February is 1, etc.)
        return "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"
    }
    fun getDayOfWeekAndMonthKey():String{
        val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
        val dayOfMonth = SimpleDateFormat("d", Locale.getDefault()).format(date)
        return "$dayOfWeek-$dayOfMonth"
    }
}

enum class TransactionType {
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

data class TransactionDetails(
    val id: String,
    val type: TransactionType,
    val category: String,
    val amount: Double
)