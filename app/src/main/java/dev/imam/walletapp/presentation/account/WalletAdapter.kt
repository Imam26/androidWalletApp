package dev.imam.walletapp.presentation.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.imam.walletapp.databinding.ItemWalletBinding
import dev.imam.walletapp.presentation.models.WalletModel

class WalletAdapter(
    private val onRefreshClickListener: (String) -> Unit
): RecyclerView.Adapter<WalletViewHolder>() {

    private val data = mutableListOf<WalletModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding = ItemWalletBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.onBind(data[position], onRefreshClickListener)
    }

    override fun getItemCount(): Int = data.size

    fun setItems(list: List<WalletModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}