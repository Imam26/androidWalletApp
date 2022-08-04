package dev.imam.walletapp.presentation.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.imam.walletapp.domain.models.Account
import dev.imam.walletapp.domain.models.Wallet
import dev.imam.walletapp.domain.models.WalletType
import dev.imam.walletapp.domain.repository.AccountRepository
import dev.imam.walletapp.domain.repository.DictionaryRepository
import dev.imam.walletapp.domain.repository.WalletRepository
import dev.imam.walletapp.presentation.models.BalanceModel
import dev.imam.walletapp.presentation.models.ExecutionResult
import dev.imam.walletapp.presentation.models.WalletModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepo: AccountRepository,
    private val walletRepo: WalletRepository,
    private val dictRepo: DictionaryRepository
): ViewModel() {
    val walletModelListLiveData = MutableLiveData<List<WalletModel>>()
    val balanceModelResultLiveData = MutableLiveData<ExecutionResult<List<BalanceModel>>>()
    val titlesLiveModel = MutableLiveData<List<String>>()
    private var _walletTypeId: String = ""
    private var _walletModels = mutableListOf<WalletModel>()

    fun loadWalletList(userId: String){
        balanceModelResultLiveData.value = ExecutionResult.loading()
        accountRepo.getUserAccounts(userId)
            .combine(walletRepo.getWallets()) { accounts, wallets ->
                _walletModels = getWalletModels(accounts, wallets)
                walletModelListLiveData.value = _walletModels
                    .filter { it.walletTypeId == _walletTypeId || _walletTypeId.isEmpty() }
                return@combine _walletModels
            }
            .combine(dictRepo.getWalletTypes()) { walletModelList, walletTypes ->
                val newWalletTypes = walletTypes.toMutableList()
                newWalletTypes.add(0, WalletType("All", ""))
                val balanceModel = getBalanceModels(walletModelList, newWalletTypes)
                balanceModelResultLiveData.value = ExecutionResult.success(balanceModel)
                titlesLiveModel.value = getTitles(balanceModel)
            }
            .catch { throwable ->
                balanceModelResultLiveData.value = ExecutionResult.error(throwable.message!!)
            }
            .launchIn(viewModelScope)
    }

    fun sortWalletModels(walletTypeId: String){
        _walletTypeId = walletTypeId
        walletModelListLiveData.value = _walletModels
            .filter { it.walletTypeId == walletTypeId || walletTypeId.isEmpty() }
    }

    private fun getTitles(balanceModelList: List<BalanceModel>): List<String>{
        val titles = mutableListOf<String>()
        balanceModelList.forEach {
            titles.add(it.walletTypeName)
        }
        return titles
    }

    private fun getWalletModels(accounts: List<Account>, wallets: List<Wallet>): MutableList<WalletModel>{
        val walletModeList = mutableListOf<WalletModel>()
        wallets.forEach {
            val filteredAccount = accounts.filter { item -> item.walletId == it.id }
            if(filteredAccount.isNotEmpty()){
                val walletModel = WalletModel()
                walletModel.id = it.id ?: ""
                walletModel.name = it.name
                walletModel.iconUrl = it.iconUrl
                walletModel.accountCount = filteredAccount.count()
                walletModel.walletTypeId = it.typeId
                walletModel.balance = filteredAccount.sumOf { fa -> fa.balance!! }
                walletModel.description = getWalletModelDescription(walletModel.balance, filteredAccount.size)
                walletModeList.add(0, walletModel)
            }
        }
        return walletModeList
    }

    private fun getBalanceModels(walletModelList: MutableList<WalletModel>, walletTypes: List<WalletType>)
    : MutableList<BalanceModel> {
        val balanceModelList = mutableListOf<BalanceModel>()
        walletTypes.sortedBy { it.id }.forEach {
            val filtered = walletModelList.filter { item -> item.walletTypeId == it.id || it.id == ""}
            if(filtered.isNotEmpty()){
                val balanceModel = getBalanceModel(it, filtered)
                balanceModelList.add(balanceModel)
            }
        }
        return balanceModelList
    }

    private fun getBalanceModel(walletType:WalletType, walletModelList: List<WalletModel>): BalanceModel {
        val balanceModel = BalanceModel()
        balanceModel.id = walletType.id ?: ""
        balanceModel.currency = "USD"
        balanceModel.balance = walletModelList.sumOf { it.balance } * 100 / 100
        balanceModel.description = getBalanceModelDescription(walletModelList.sumOf { it.accountCount })
        balanceModel.walletTypeName = walletType.name
        return balanceModel
    }

    private fun getWalletModelDescription(balance: Double, accountSize: Int):String {
        return "$balance USD From - $accountSize Account"
    }

    private fun getBalanceModelDescription(walletSize: Int):String {
        return "Active Balance ($walletSize Account)"
    }
}