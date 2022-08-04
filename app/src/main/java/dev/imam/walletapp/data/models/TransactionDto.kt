package dev.imam.walletapp.data.models

import com.google.firebase.database.DataSnapshot

data class TransactionDto(
    var id:String,
    var name: String,
    var sum: Double,
    var time: String,
    var type: Int,
    var accountId:String,

    var accountName: String,
    var walletId: String,
    var balance: Double
) {
    constructor() : this("", "",0.0, "", 0, "", "", "", 0.0)

    fun convert(dataSnapshot: DataSnapshot){
        if(dataSnapshot.key == null) return

        id = dataSnapshot.key!!
        name = dataSnapshot.child("name").value.toString()
        sum = dataSnapshot.child("sum").value.toString().toDouble()
        time = dataSnapshot.child("time").value.toString()
        type = dataSnapshot.child("type").value.toString().toInt()
        accountId = dataSnapshot.child("accountId").value.toString()
    }
}