package dev.imam.walletapp.presentation.transaction

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemIncomeExpenseDboardBinding
import dev.imam.walletapp.presentation.models.IncomeExpenseDboardModel

class IncomeExpenseDboardAdapter: RecyclerView.Adapter<IncomeExpenseDboardViewHolder>() {
    private val data = mutableListOf<IncomeExpenseDboardModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeExpenseDboardViewHolder {
        val binding = ItemIncomeExpenseDboardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return IncomeExpenseDboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeExpenseDboardViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(value: List<IncomeExpenseDboardModel>){
        data.clear()
        data.addAll(0, value)
        notifyDataSetChanged()
    }
}