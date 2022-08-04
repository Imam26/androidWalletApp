package dev.imam.walletapp.data.mappers

import dev.imam.walletapp.data.models.TransactionDto
import dev.imam.walletapp.domain.models.Transaction
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class TransactionMapper {
    fun map(target: Transaction, source: TransactionDto): Transaction {
        target.id = source.id
        target.name = source.name
        target.sum = source.sum
        target.date = SimpleDateFormat("yyyy-MM-dd").parse(source.time)!!
        target.type = source.type
        target.accountId = source.accountId
        target.accountName = source.accountName
        target.walletId = source.walletId
        target.balance = source.balance
        return target
    }
}