package dev.imam.walletapp.presentation.addaccount

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.marginStart
import com.bumptech.glide.Glide
import dev.imam.walletapp.databinding.ItemSpinnerBinding
import dev.imam.walletapp.presentation.models.WalletModel


class SpinnerAdapterWithIcon(
    context: Context,
    textViewResourceId:Int,
    private val objects: Array<WalletModel>
): ArrayAdapter<WalletModel>(context, textViewResourceId, objects) {
    private lateinit var binding: ItemSpinnerBinding

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup?): View {
        binding = ItemSpinnerBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.nameTv.text = objects[position].name

        if(objects[position].iconUrl.isNotEmpty()){
            Glide.with(context)
                .asBitmap()
                .load(objects[position].iconUrl)
                .timeout(6000)
                .into(binding.iconIv)
        } else {
            binding.iconIv.visibility = View.GONE
        }

        return binding.root
    }
}