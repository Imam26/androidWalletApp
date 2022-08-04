package dev.imam.walletapp.domain.repository

import dev.imam.walletapp.domain.models.Wallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getWallets(): Flow<List<Wallet>>
}