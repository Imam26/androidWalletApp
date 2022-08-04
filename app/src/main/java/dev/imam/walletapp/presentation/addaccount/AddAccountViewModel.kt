package dev.imam.walletapp.presentation.addaccount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.imam.walletapp.domain.models.Account
import dev.imam.walletapp.domain.repository.AccountRepository
import dev.imam.walletapp.domain.repository.WalletRepository
import dev.imam.walletapp.presentation.models.AccountModel
import dev.imam.walletapp.presentation.models.ExecutionResult
import dev.imam.walletapp.presentation.models.WalletModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val walletRepo: WalletRepository,
    private val accountRepo: AccountRepository
): ViewModel() {
    val walletModelListLiveData = MutableLiveData<ExecutionResult<List<WalletModel>>>()
    val saveAccountResultLiveData = MutableLiveData<ExecutionResult<String>>()

    fun loadWalletList(typeId: String){
        walletModelListLiveData.value = ExecutionResult.loading()
        viewModelScope.launch {
            walletRepo.getWallets().collect {
                val walletModelList = mutableListOf<WalletModel>()

                it.filter { item -> item.typeId == typeId }.forEach { item ->
                    val walletModel = WalletModel()
                    walletModel.id = item.id!!
                    walletModel.name = item.name
                    walletModel.iconUrl = item.iconUrl
                    walletModelList.add(walletModel)
                }

                walletModelList.add(0, WalletModel("", "Select item"))
                walletModelListLiveData.value  = ExecutionResult.success(walletModelList)
            }
        }
    }

    fun saveAccount(model: AccountModel) {
        saveAccountResultLiveData.value = ExecutionResult.loading()
        val account = Account(model.name, model.balance, model.walletId, model.userId)
        viewModelScope.launch {
            try {
                accountRepo.saveAccount(account).collect {
                    saveAccountResultLiveData.value = ExecutionResult.success(it)
                }
            }
            catch (ex: Throwable){
                saveAccountResultLiveData.value = ExecutionResult.error(ex.message!!)
            }
        }

    }
}