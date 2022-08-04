package dev.imam.walletapp.presentation.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemIncomeExpenseBinding
import dev.imam.walletapp.databinding.ItemIncomeExpenseDboardBinding
import dev.imam.walletapp.presentation.models.IncomeExpenseDboardModel
import dev.imam.walletapp.presentation.models.IncomeExpenseModel

class IncomeExpenseAdapter: RecyclerView.Adapter<IncomeExpenseViewHolder>() {
    private val data = mutableListOf<IncomeExpenseModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeExpenseViewHolder {
        val binding = ItemIncomeExpenseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return IncomeExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeExpenseViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(value: List<IncomeExpenseModel>){
        data.clear()
        data.addAll(0, value)
        notifyDataSetChanged()
    }
}