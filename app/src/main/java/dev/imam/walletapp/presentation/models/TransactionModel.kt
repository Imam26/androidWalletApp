package dev.imam.walletapp.presentation.models

data class TransactionModel(
    var description: String,
    var sum: Double,
    var type: Int,
    var name: String,
    var time: String,
    var icon: String
){
    constructor():this("", 0.0, 0, "", "", "")
}