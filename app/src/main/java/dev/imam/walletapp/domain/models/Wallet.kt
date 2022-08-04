package dev.imam.walletapp.domain.models

import com.google.firebase.database.DataSnapshot

data class Wallet(
    var iconUrl:String = "",
    var name: String = "",
    var typeId:String = "",
    var id:String? = ""
){
    fun convert(dataSnapshot: DataSnapshot){
        id = dataSnapshot.key
        name = dataSnapshot.child("name").value.toString()
        iconUrl = dataSnapshot.child("iconUrl").value.toString()
        typeId = dataSnapshot.child("typeId").value.toString()
    }
}