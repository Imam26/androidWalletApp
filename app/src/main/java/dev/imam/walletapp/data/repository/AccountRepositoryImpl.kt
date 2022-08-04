package dev.imam.walletapp.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dev.imam.walletapp.domain.models.Account
import dev.imam.walletapp.domain.repository.AccountRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AccountRepositoryImpl(
    private val database: DatabaseReference
): AccountRepository {

    override fun getUserAccounts(userId: String): Flow<List<Account>>  {
        val result = mutableListOf<Account>()
        return callbackFlow {
            val accountListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    result.clear()
                    dataSnapshot.children.forEach{ snapshot ->
                        val account = Account()
                        account.convert(snapshot)
                        result.add(0, account)
                    }
                    this@callbackFlow.trySend(result)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    this@callbackFlow.cancel(databaseError.message)
                }
            }
            database.child("/db/accounts").orderByChild("userId").equalTo(userId).addValueEventListener(accountListener)

            this@callbackFlow.awaitClose {
                database.child("/db/accounts").orderByChild("userId").equalTo(userId)
                    .removeEventListener(accountListener)
            }
        }
    }

    override fun saveAccount(model: Account): Flow<String> {
        return callbackFlow {
            val newRef = database.child("/db/accounts").push()
            newRef.setValue(model)
                .addOnSuccessListener {
                    this@callbackFlow.trySend(newRef.key!!)
                }
                .addOnFailureListener{
                    this@callbackFlow.cancel(it.message!!)
                }

            this@callbackFlow.awaitClose()
        }
    }
}