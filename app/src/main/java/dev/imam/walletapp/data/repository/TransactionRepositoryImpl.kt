package dev.imam.walletapp.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dev.imam.walletapp.data.mappers.TransactionMapper
import dev.imam.walletapp.data.models.TransactionDto
import dev.imam.walletapp.domain.models.Account
import dev.imam.walletapp.domain.models.Transaction
import dev.imam.walletapp.domain.repository.TransactionRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TransactionRepositoryImpl(
    private val database: DatabaseReference
): TransactionRepository {
    override fun getUserTransactions(userId: String): Flow<List<Transaction>> {
        val result = mutableListOf<Transaction>()
        val accounts = mutableListOf<Account>()
        return callbackFlow {
            val transactionListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    result.clear()
                    snapshot.children.forEach { item ->
                        val accountId = item.child("accountId").value.toString()
                        val account = accounts.find { x -> x.id == accountId }
                        if(account != null){
                            val transactionDto = TransactionDto()
                            transactionDto.convert(item)
                            transactionDto.accountName = account.name!!
                            transactionDto.walletId = account.walletId
                            transactionDto.balance = account.balance!!
                            result.add(0, TransactionMapper().map(Transaction(), transactionDto))
                        }
                    }

                    this@callbackFlow.trySend(result)
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.cancel(error.message)
                }
            }

            val accountListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    accounts.clear()
                    snapshot.children.forEach{ item ->
                        val account = Account()
                        account.convert(item)
                        accounts.add(0, account)
                    }
                    database.child("/db/transactions").addValueEventListener(transactionListener)
                }

                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.cancel(error.message)
                }
            }

            database.child("/db/accounts").orderByChild("userId").equalTo(userId).addValueEventListener(accountListener)

            this@callbackFlow.awaitClose {
                database.child("db/transactions").removeEventListener(transactionListener)
                database.child("/db/accounts").orderByChild("userId").equalTo(userId)
                    .removeEventListener(accountListener)
            }
        }
    }
}