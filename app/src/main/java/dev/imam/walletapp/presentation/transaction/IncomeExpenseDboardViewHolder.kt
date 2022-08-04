package dev.imam.walletapp.presentation.transaction

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import dev.imam.walletapp.databinding.ItemIncomeExpenseDboardBinding
import dev.imam.walletapp.presentation.models.IncomeExpenseDboardModel
import dev.imam.walletapp.presentation.models.IncomeExpenseModel

class IncomeExpenseDboardViewHolder(
    private val binding: ItemIncomeExpenseDboardBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind(model: IncomeExpenseDboardModel){
        setupViewTabLayoutWithViewPager(model.incomeExpenseList)
    }

    private fun setupViewTabLayoutWithViewPager(data: List<IncomeExpenseModel>){
        val incomeExpenseAdapter = IncomeExpenseAdapter()
        incomeExpenseAdapter.setData(data)

        binding.viewPager.adapter = incomeExpenseAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = data[position].period
        }.attach()
    }
}