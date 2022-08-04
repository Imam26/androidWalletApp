package dev.imam.walletapp.presentation.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dev.imam.walletapp.R
import dev.imam.walletapp.databinding.FragmentTransactionBinding
import dev.imam.walletapp.domain.constants.TransactionType
import dev.imam.walletapp.presentation.account.WalletAdapter
import dev.imam.walletapp.presentation.models.IncomeExpenseDboardModel
import dev.imam.walletapp.presentation.models.IncomeExpenseModel
import dev.imam.walletapp.presentation.models.ResultType
import dev.imam.walletapp.presentation.models.TransactionModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var incomeExpenseDboardAdapter: IncomeExpenseDboardAdapter
    private  lateinit var transactionItemAdapter: TransactionItemAdapter
    private val viewModel: TransactionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        setupViewPager()
        setupRecyclerView()
        setupObservers()

        val firebaseAuth = FirebaseAuth.getInstance()
        viewModel.loadTransactions(firebaseAuth.currentUser!!.uid)

        return binding.root
    }

    private fun setupObservers(){
        viewModel.transactionModelResultLiveData.observe(viewLifecycleOwner) {
            when(it.resultType){
                ResultType.Success -> {
                    if(it.data.isNullOrEmpty()){
                        noDataState()
                    } else {
                        dataExistState()
                        transactionItemAdapter.setData(it.data!!)
                    }
                }
                ResultType.Error -> {
                    errorState()
                    Toast.makeText(requireContext(), "Error: " + it.message, Toast.LENGTH_SHORT).show()
                }
                ResultType.Loading -> {
                    startState()
                }
                else -> false
            }
        }

        viewModel.transactionDboardModelResultLiveData.observe(viewLifecycleOwner) {
            when(it.resultType){
                ResultType.Success ->
                    incomeExpenseDboardAdapter.setData(it.data!!)
                else -> false
            }
        }
    }

    private fun setupViewPager(){
        incomeExpenseDboardAdapter = IncomeExpenseDboardAdapter()
        binding.viewPager.adapter = incomeExpenseDboardAdapter
        binding.indicator.attachToPager(binding.viewPager)
    }

    private fun setupRecyclerView(){
        transactionItemAdapter = TransactionItemAdapter()

        val transactionLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.currencyRecyclerView.apply {
            adapter = transactionItemAdapter
            layoutManager = transactionLayoutManager
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        }
    }

    private fun noDataState() {
        binding.notDataTv.visibility = View.VISIBLE
        binding.viewPager.visibility = View.GONE
        binding.currencyRecyclerView.visibility = View.GONE
        binding.pBar.visibility = View.GONE
    }

    private fun dataExistState(){
        binding.viewPager.visibility = View.VISIBLE
        binding.currencyRecyclerView.visibility = View.VISIBLE
        binding.notDataTv.visibility = View.GONE
        binding.pBar.visibility = View.GONE
    }

    private fun startState(){
        binding.notDataTv.visibility = View.GONE
        binding.viewPager.visibility = View.GONE
        binding.currencyRecyclerView.visibility = View.GONE
        binding.pBar.visibility = View.VISIBLE
    }

    private fun errorState(){
        binding.pBar.visibility = View.GONE
        binding.notDataTv.visibility = View.GONE
    }

    /*private fun getRecyclerViewData() : List<TransactionModel>{
        return listOf(
            TransactionModel("PriorBank DK1214", 32.0, TransactionType.EXPOSE, "Cafe, restaurant", "Yesterday", ""),
            TransactionModel("PriorBank DK1214", 32.0, TransactionType.INCOME, "Cafe, restaurant", "12 april 2022", ""),
            TransactionModel("PriorBank DK1214", 32.0, TransactionType.EXPOSE, "Cafe, restaurant", "", "")
        )
    } */

    /*private fun getData(): List<IncomeExpenseDboardModel>{
        return listOf(
            IncomeExpenseDboardModel(listOf(
                IncomeExpenseModel("USD 512", "7 days", "+723.44 USD", "-213.123 USD"),
                IncomeExpenseModel("USD 1024","30 days", "+724.12 USD", "-212.123 USD"),
                IncomeExpenseModel("USD 2048","1 month", "+725.12 USD", "-211.123 USD"),
                IncomeExpenseModel("USD 4096","3 month", "+726.123 USD", "-214.122 USD"),
            )),
            IncomeExpenseDboardModel(listOf(
                IncomeExpenseModel("USD 512", "7 days", "+723 USD", "-213 USD"),
                IncomeExpenseModel("USD 1024","30 days", "+724 USD", "-212 USD"),
                IncomeExpenseModel("USD 2048","1 month", "+725 USD", "-211 USD"),
                IncomeExpenseModel("USD 4096","3 month", "+726 USD", "-214 USD"),
            )),
            IncomeExpenseDboardModel(listOf(
                IncomeExpenseModel("USD 512", "7 days", "+723 USD", "-213 USD"),
                IncomeExpenseModel("USD 1024","30 days", "+724 USD", "-212 USD"),
                IncomeExpenseModel("USD 2048","1 month", "+725 USD", "-211 USD"),
                IncomeExpenseModel("USD 4096","3 month", "+726 USD", "-214 USD"),
            )),
        )
    }*/
}