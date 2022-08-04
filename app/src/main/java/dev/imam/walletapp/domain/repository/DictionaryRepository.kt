package dev.imam.walletapp.domain.repository

import dev.imam.walletapp.domain.models.WalletType
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getWalletTypes(): Flow<List<WalletType>>
}