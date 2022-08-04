package dev.imam.walletapp.presentation.transaction

import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemIncomeExpenseBinding
import dev.imam.walletapp.presentation.models.IncomeExpenseModel

class IncomeExpenseViewHolder(
    private val binding: ItemIncomeExpenseBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind(model: IncomeExpenseModel){
        binding.balanceTv.text = model.balance
        binding.balanceExpenseTv.text = model.expense
        binding.balanceIncomeTv.text = model.income
    }
}