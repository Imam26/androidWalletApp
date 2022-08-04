package dev.imam.walletapp.presentation.models

data class BalanceModel(
    var id:String = "",
    var description: String = "",
    var currency: String = "",
    var balance: Double = 0.0,
    var walletTypeName:String = "",
    //var walletTypeId: String = ""
)