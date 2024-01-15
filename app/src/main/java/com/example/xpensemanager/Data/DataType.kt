package com.example.xpensemanager.Data

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