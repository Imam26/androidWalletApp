package dev.imam.walletapp.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dev.imam.walletapp.domain.models.Wallet
import dev.imam.walletapp.domain.repository.WalletRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WalletRepositoryImpl(
    private val database: DatabaseReference
): WalletRepository {

    override fun getWallets(): Flow<List<Wallet>> {
        val result = mutableListOf<Wallet>()
        return callbackFlow {
            val walletListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    result.clear()
                    dataSnapshot.children.forEach{ snapshot ->
                        val wallet = Wallet()
                        wallet.convert(snapshot)
                        result.add(0, wallet)
                    }
                    this@callbackFlow.trySend(result)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            database.child("/db/wallets").addValueEventListener(walletListener)

            this@callbackFlow.awaitClose {
                database.child("/db/wallets")
                    .removeEventListener(walletListener)
            }
        }
    }
}