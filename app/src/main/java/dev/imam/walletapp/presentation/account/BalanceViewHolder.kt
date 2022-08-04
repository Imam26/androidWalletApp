package dev.imam.walletapp.presentation.account

import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemBalanceBinding
import dev.imam.walletapp.presentation.models.BalanceModel

class BalanceViewHolder(
    private val binding: ItemBalanceBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(model: BalanceModel){
        binding.balanceTv.text = model.balance.toString()
        binding.currencyTv.text = model.currency
        binding.labelTv.text = model.description
    }
}