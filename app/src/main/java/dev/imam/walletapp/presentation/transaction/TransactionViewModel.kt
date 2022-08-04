package dev.imam.walletapp.presentation.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.imam.walletapp.domain.constants.CurrencyType
import dev.imam.walletapp.domain.constants.TransactionType
import dev.imam.walletapp.domain.models.Transaction
import dev.imam.walletapp.domain.models.Wallet
import dev.imam.walletapp.domain.repository.TransactionRepository
import dev.imam.walletapp.domain.repository.WalletRepository
import dev.imam.walletapp.presentation.models.ExecutionResult
import dev.imam.walletapp.presentation.models.IncomeExpenseDboardModel
import dev.imam.walletapp.presentation.models.IncomeExpenseModel
import dev.imam.walletapp.presentation.models.TransactionModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val walletRepo: WalletRepository
): ViewModel() {

    val transactionModelResultLiveData = MutableLiveData<ExecutionResult<List<TransactionModel>>>()
    val transactionDboardModelResultLiveData = MutableLiveData<ExecutionResult<List<IncomeExpenseDboardModel>>>()

    fun loadTransactions(userId: String){
        transactionModelResultLiveData.value = ExecutionResult.loading()
        transactionRepository.getUserTransactions(userId)
            .combine(walletRepo.getWallets()) { transactions, wallets ->
                setTransaction(transactions, wallets)
                setTransactionDboard(transactions, wallets)
            }
            .catch { cause: Throwable ->
                transactionModelResultLiveData.value = ExecutionResult.error(cause.message!!)
            }
            .launchIn(viewModelScope)
    }

    private fun setTransaction(transactions: List<Transaction>, wallets: List<Wallet>) {
        val sortedTransaction = transactions.sortedByDescending { x -> x.date }
        var currentTime = ""
        val transactionModelList = mutableListOf<TransactionModel>()

        sortedTransaction.forEach { item ->
            val dateString =  SimpleDateFormat("dd MMMM yyyy").format(item.date)
            val transactionModel = TransactionModel()
            val wallet = wallets.find { x -> x.id == item.walletId }!!

            transactionModel.description = wallet.name + " " + item.accountName
            transactionModel.name = item.name
            transactionModel.sum = item.sum
            transactionModel.type = item.type
            transactionModel.icon = wallet.iconUrl

            if(currentTime != dateString){
                currentTime = dateString
                transactionModel.time = dateString
            }

            transactionModelList.add(transactionModel)
        }

        transactionModelResultLiveData.value = ExecutionResult.success(transactionModelList)
    }

    private fun setTransactionDboard(transactions: List<Transaction>, wallets: List<Wallet>) {
        val dboardModelList = mutableListOf<IncomeExpenseDboardModel>()

        val walletsIds = wallets.map { item -> item.typeId }.sorted().distinct().toMutableList()
        walletsIds.add(0, "")

        walletsIds.forEach { item ->
            val filteredTransactions = filterTransactionByWalletTypeId(transactions, wallets, item)

            val incomeExpenseModelList = mutableListOf<IncomeExpenseModel>()
            incomeExpenseModelList.add(getIncomeExpenseModelByDate(filteredTransactions, 7, 0))
            incomeExpenseModelList.add(getIncomeExpenseModelByDate(filteredTransactions, 30, 0))
            incomeExpenseModelList.add(getIncomeExpenseModelByDate(filteredTransactions, 0, 2))
            incomeExpenseModelList.add(getIncomeExpenseModelByDate(filteredTransactions, 0, 3))

            val dboardModel = IncomeExpenseDboardModel(incomeExpenseModelList)
            dboardModelList.add(dboardModel)
        }

        transactionDboardModelResultLiveData.value = ExecutionResult.success(dboardModelList)
    }

    private fun filterTransactionByWalletTypeId(transactions: List<Transaction>, wallets: List<Wallet>, typeId: String)
        : List<Transaction> {
        if(typeId == "") return transactions

        return transactions.filter { item ->
            wallets.any { x -> x.id == item.walletId && x.typeId == typeId }
        }
    }

    private fun getIncomeExpenseModelByDate(transactions: List<Transaction>, day: Int, month:Int): IncomeExpenseModel {
        val model = IncomeExpenseModel()
        val checkDate = Calendar.getInstance()
        checkDate.add(Calendar.MONTH, -1 * month)
        checkDate.add(Calendar.DAY_OF_YEAR, -1 * day)

        if (month > 0){
            model.period = "$month month "
        }

        if(day > 0) {
            model.period += "$day days"
        }

        val incomeSum = transactions
            .filter { x -> x.date > checkDate.time && x.type == TransactionType.INCOME }
            .sumOf { item -> item.sum }

        model.income = "+" + incomeSum.toString() + " " + CurrencyType.USD

        val exposeSum = transactions
            .filter { x -> x.date > checkDate.time && x.type == TransactionType.EXPOSE }
            .sumOf { item -> item.sum }

        model.expense = "-" + exposeSum.toString() + " " + CurrencyType.USD

        model.balance = CurrencyType.USD + " " + (incomeSum - exposeSum).toString()
        return model
    }
}