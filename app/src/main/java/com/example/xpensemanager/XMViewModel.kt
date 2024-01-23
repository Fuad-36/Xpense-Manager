package com.example.xpensemanager

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.xpensemanager.Data.Event
import com.example.xpensemanager.Data.Transaction
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
import kotlin.math.log

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

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
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
}

