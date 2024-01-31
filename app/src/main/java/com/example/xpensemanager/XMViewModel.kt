package com.example.xpensemanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.xpensemanager.Data.Event
import com.example.xpensemanager.Data.Transaction
import com.example.xpensemanager.Data.TransactionType
import com.example.xpensemanager.Data.USER_NODE
import com.example.xpensemanager.Data.UserData
import com.example.xpensemanager.Screens.MonthYearPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import java.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.xpensemanager.Data.TransactionDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@HiltViewModel
class XMViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore

) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    private var _selectedDate = mutableStateOf(Calendar.getInstance())
    val selectedDate: MutableState<Calendar> = _selectedDate
    private val _transactions = mutableStateOf<List<Transaction>>(emptyList())
    val transactions: State<List<Transaction>> = _transactions
    var isTransactionScreenVisible by mutableStateOf(false)


    val transactionsMap: State<Map<String, Map<String, MutableList<TransactionDetails>>>> = derivedStateOf {
        transactions.value.groupBy { transaction ->
            // Use the month-year as the outer key
            transaction.getMonthYearKey()
        }.mapValues { (_, transactions) ->
            // For each month-year group, create a map with day of the week-day of the month as the key
            transactions.groupBy { transaction ->
                transaction.getDayOfWeekAndMonthKey()
            }.mapValues { (_, transactionsOfDay) ->
                // For each day, create a list of TransactionDetails
                transactionsOfDay.map { transaction ->
                    TransactionDetails(
                        id = transaction.id,
                        type = transaction.type,
                        category = transaction.category,
                        amount = transaction.amount
                    )
                }.toMutableList()
            }
        }
    }


    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
        currentUser?.uid?.let {
            fetchTransactionsAndUpdateState(it)
        }

    }

    fun signUp(name: String, email: String, password: String) {
        inProcess.value = true
        if (name.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("name", name).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signedIn.value = true
                        createOrUpdateProfile(name, email)
                    } else {
                        handleException(it.exception, customMessage = "Sign Up failed")
                    }
                }
            } else {
                handleException(customMessage = "Name already exists")
                inProcess.value = false
            }

        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                signedIn.value = true
                createOrUpdateProfile(name, email)
            } else {
                handleException(it.exception, customMessage = "Sign Up failed")
            }
        }
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all the fields")
            return
        } else {
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signedIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(exception = it.exception, customMessage = "Login failed")
                    }
                }
        }
    }

    fun updateMonthYear(month: Int, year: Int) {
        val updatedCalendar = _selectedDate.value.apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }
        _selectedDate.value = updatedCalendar
    }
    fun onTransactionSave(transaction: Transaction,context:Context){
        // Add transaction to the local state
        if(transaction.category.isEmpty() or transaction.amount.equals(0.0)) {
            handleException(customMessage = "Please fill all the fields")
            Toast.makeText(context,"please fill",Toast.LENGTH_SHORT).show()
            return
        }
        _transactions.value = _transactions.value + transaction

        // Add transaction to Firestore
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            val userRef = FirebaseFirestore.getInstance().collection("user").document(currentUserUid)
            val transactionsRef = userRef.collection("transactions")

            transactionsRef.add(transaction)

        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("XMApp", "XM exception: ", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun createOrUpdateProfile(name: String? = null, email: String? = null) {
        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            email = email ?: userData.value?.email
        )
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    // update user data

                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProcess.value = false
                    getUserData(uid)
                }
            }
                .addOnFailureListener {
                    handleException(it, "Cannot Retrieve User")
                }
        }
    }

    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Can not retrieve User")
            }
            if (value != null) {
                var user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
            }
        }
    }
    fun convertLocalDateToDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
    fun fetchTransactionsAndUpdateState(currentUserUid: String) {
        val userRef = FirebaseFirestore.getInstance().collection("user").document(currentUserUid)
        val transactionsRef = userRef.collection("transactions")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = transactionsRef.get().await()

                val transactionsList = mutableListOf<Transaction>()

                for (document in querySnapshot.documents) {
                    val transaction = document.toObject(Transaction::class.java)
                    transaction?.let {
                        transactionsList.add(it)
                    }
                }

                // Update the mutable state with the fetched transactions
                _transactions.value = transactionsList

            } catch (e: Exception) {
                // Handle the exception
                handleException(e,"Cannot retrieve the thransaciton data")
            }
        }
    }


}

