package dev.imam.walletapp.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.imam.walletapp.data.repository.AccountRepositoryImpl
import dev.imam.walletapp.data.repository.DictionaryRepositoryImpl
import dev.imam.walletapp.data.repository.TransactionRepositoryImpl
import dev.imam.walletapp.data.repository.WalletRepositoryImpl
import dev.imam.walletapp.domain.repository.AccountRepository
import dev.imam.walletapp.domain.repository.DictionaryRepository
import dev.imam.walletapp.domain.repository.TransactionRepository
import dev.imam.walletapp.domain.repository.WalletRepository
import dev.imam.walletapp.presentation.account.AccountViewModel
import dev.imam.walletapp.presentation.addaccount.AddAccountViewModel
import dev.imam.walletapp.presentation.transaction.TransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { Firebase.database.reference }

    factory<AccountRepository> { AccountRepositoryImpl(get()) }

    factory<WalletRepository> { WalletRepositoryImpl(get())  }

    factory<DictionaryRepository> { DictionaryRepositoryImpl(get())  }

    factory<TransactionRepository> { TransactionRepositoryImpl(get())  }

    viewModel { AccountViewModel(get(), get(), get()) }

    viewModel { TransactionViewModel(get(), get()) }

    viewModel { AddAccountViewModel(get(), get()) }
}