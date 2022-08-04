package dev.imam.walletapp.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dev.imam.walletapp.domain.models.WalletType
import dev.imam.walletapp.domain.repository.DictionaryRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DictionaryRepositoryImpl(
    private val database: DatabaseReference
): DictionaryRepository {

    override fun getWalletTypes(): Flow<List<WalletType>> {
        val result = mutableListOf<WalletType>()
        return callbackFlow {
            val walletTypeListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    result.clear()
                    dataSnapshot.children.forEach{ snapshot ->
                        val walletType = WalletType()
                        walletType.convert(snapshot)
                        result.add(0, walletType)

                    }
                    this@callbackFlow.trySend(result)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            database.child("/db/wallettypes").addValueEventListener(walletTypeListener)

            this@callbackFlow.awaitClose {
                database.child("/db/wallettypes")
                    .removeEventListener(walletTypeListener)
            }
        }
    }
}