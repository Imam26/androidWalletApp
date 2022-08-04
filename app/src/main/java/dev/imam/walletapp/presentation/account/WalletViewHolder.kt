package dev.imam.walletapp.presentation.account

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.imam.walletapp.databinding.ItemWalletBinding
import dev.imam.walletapp.presentation.models.WalletModel

class WalletViewHolder(
    private val binding: ItemWalletBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(wallet: WalletModel, onRefreshClickListener: (String)->Unit){
        binding.nameTv.text = wallet.name
        binding.descriptionTv.text = wallet.description

        binding.refreshIv.setOnClickListener {
            onRefreshClickListener(wallet.id)
        }

        Glide.with(this.itemView)
            .asBitmap()
            .load(wallet.iconUrl)
            .timeout(6000)
            .into(binding.iconIv)
    }
}