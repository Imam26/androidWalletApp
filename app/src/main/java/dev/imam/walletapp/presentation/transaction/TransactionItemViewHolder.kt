package dev.imam.walletapp.presentation.transaction

import android.graphics.Color
import android.graphics.Color.green
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.imam.walletapp.R
import dev.imam.walletapp.databinding.ItemTransactionBinding
import dev.imam.walletapp.domain.constants.TransactionType
import dev.imam.walletapp.presentation.models.TransactionModel

class TransactionItemViewHolder(
    val binding: ItemTransactionBinding
): RecyclerView.ViewHolder(binding.root) {
    fun onBind(model: TransactionModel){
        Glide.with(this.itemView)
            .asBitmap()
            .load(model.icon)
            .timeout(6000)
            .into(binding.iconIv)

        if(model.time.isNotEmpty()){
            binding.transactTimeTv.text = model.time
            binding.transactTimeTv.visibility = View.VISIBLE
        } else {
            binding.transactTimeTv.visibility = View.GONE
        }

        binding.nameTv.text = model.name
        binding.descriptionTv.text = model.description

        if(model.type == TransactionType.INCOME){
            binding.incomeExpenseSumTv.text = "+ " + model.sum.toString()
            binding.incomeExpenseSumTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
        } else {
            binding.incomeExpenseSumTv.text = "- " + model.sum.toString()
            binding.incomeExpenseSumTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
        }
    }
}