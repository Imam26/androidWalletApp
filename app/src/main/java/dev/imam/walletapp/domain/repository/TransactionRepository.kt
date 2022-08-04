package dev.imam.walletapp.domain.repository

import dev.imam.walletapp.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getUserTransactions(userId: String): Flow<List<Transaction>>
}