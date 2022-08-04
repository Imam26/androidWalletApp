package dev.imam.walletapp.domain.repository

import dev.imam.walletapp.domain.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getUserAccounts(userId: String): Flow<List<Account>>
    fun saveAccount(model: Account): Flow<String>
}