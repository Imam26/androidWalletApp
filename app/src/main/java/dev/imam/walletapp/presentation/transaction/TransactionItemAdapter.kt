package dev.imam.walletapp.presentation.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemTransactionBinding
import dev.imam.walletapp.presentation.models.TransactionModel

class TransactionItemAdapter: RecyclerView.Adapter<TransactionItemViewHolder>() {
    private val data = mutableListOf<TransactionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val binding = ItemTransactionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun setData(value: List<TransactionModel>){
        data.clear()
        data.addAll(0, value)
        notifyDataSetChanged()
    }
}