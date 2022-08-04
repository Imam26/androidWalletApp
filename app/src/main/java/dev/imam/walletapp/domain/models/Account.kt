package dev.imam.walletapp.domain.models

import com.google.firebase.database.DataSnapshot


data class Account(
    var name: String? = "",
    var balance: Double? = 0.0,
    var walletId:String = "",
    var userId:String? = "",
    var id:String? = null
) {
    fun convert(dataSnapshot: DataSnapshot){
        id = dataSnapshot.key
        name = dataSnapshot.child("name").value.toString()
        balance = dataSnapshot.child("balance").value.toString().toDouble()
        walletId = dataSnapshot.child("walletId").value.toString()
        userId = dataSnapshot.child("userId").value.toString()
    }
}
