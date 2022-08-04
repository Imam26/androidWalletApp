package dev.imam.walletapp.presentation.addaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import dev.imam.walletapp.R
import dev.imam.walletapp.databinding.FragmentWalletTypeBinding
import dev.imam.walletapp.domain.constants.WalletType

class WalletTypeFragment : Fragment() {

    private lateinit var binding: FragmentWalletTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWalletTypeBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.bankAccountCv.setOnClickListener {
            findNavController().navigate(R.id.action_walletTypeFragment_to_addAccountFragment,
            bundleOf(WALLET_TYPE to WalletType.Banks))
        }

        binding.cryptoAccountCv.setOnClickListener {
            findNavController().navigate(R.id.action_walletTypeFragment_to_addAccountFragment,
                bundleOf(WALLET_TYPE to WalletType.Crypto))
        }

        return binding.root
    }
}