package dev.imam.walletapp.presentation.models

data class WalletModel(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var iconUrl: String = "",
    var walletTypeId: String = "",
    var balance: Double = 0.0,
    var accountCount:Int = 0
)