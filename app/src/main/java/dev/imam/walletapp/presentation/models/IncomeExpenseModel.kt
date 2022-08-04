package dev.imam.walletapp.presentation.models

data class
IncomeExpenseModel(
    var balance:String,
    var period: String,
    var income : String,
    var expense: String
) {
    constructor(): this("","","","")
}