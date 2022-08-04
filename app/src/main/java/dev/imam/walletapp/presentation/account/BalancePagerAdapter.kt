package dev.imam.walletapp.presentation.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import dev.imam.walletapp.databinding.ItemBalanceBinding
import dev.imam.walletapp.databinding.ItemWalletBinding
import dev.imam.walletapp.presentation.models.BalanceModel

class BalancePagerAdapter : RecyclerView.Adapter<BalanceViewHolder>() {

    private var data = mutableListOf<BalanceModel>()

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long =
        data[position].id.toLongOrNull() ?: 0

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val binding = ItemBalanceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BalanceViewHolder(binding)
    }

    fun setBalance(balances: List<BalanceModel>) {
        data.clear()
        data.addAll(0, balances)
        notifyDataSetChanged()
    }
}
