package dev.imam.walletapp.domain.models

import java.util.*

data class Transaction(
    var id:String,
    var name: String,
    var sum: Double,
    var date: Date,
    var type: Int,
    var accountId:String,

    var accountName: String,
    var walletId: String,
    var balance: Double
) {
    constructor() : this("", "",0.0, Date(), 0, "", "", "", 0.0)
}