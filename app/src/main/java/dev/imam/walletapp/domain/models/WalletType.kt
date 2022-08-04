package dev.imam.walletapp.domain.models

import com.google.firebase.database.DataSnapshot

data class WalletType(
    var name:String = "",
    var id:String? = ""
){
    fun convert(dataSnapshot: DataSnapshot){
        id = dataSnapshot.key
        name = dataSnapshot.child("name").value.toString()
    }
}