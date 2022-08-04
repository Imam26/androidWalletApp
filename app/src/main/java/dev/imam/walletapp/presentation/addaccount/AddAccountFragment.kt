package dev.imam.walletapp.presentation.addaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dev.imam.walletapp.R
import dev.imam.walletapp.databinding.FragmentAddAccountBinding
import dev.imam.walletapp.domain.constants.WalletType
import dev.imam.walletapp.presentation.models.AccountModel
import dev.imam.walletapp.presentation.models.ResultType
import dev.imam.walletapp.presentation.models.WalletModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val WALLET_TYPE = "WALLET_TYPE"

class AddAccountFragment : Fragment() {
    private lateinit var binding: FragmentAddAccountBinding
    private val viewModel: AddAccountViewModel by viewModel()
    private lateinit var firebaseAuth: FirebaseAuth

    private var walletType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            walletType = it.getString(WALLET_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddAccountBinding.inflate(inflater, container, false)

        walletType?.let {
            viewModel.loadWalletList(it)
        }

        firebaseAuth = FirebaseAuth.getInstance()

        setSpinnerLabel()
        setupObservers()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            if(!isValid()) return@setOnClickListener

            val selectedWallet = binding.walletSp.selectedItem as WalletModel
            val accountName = binding.accountNameEt.text.toString()
            val balance = binding.balanceNameEt.text.toString()
            val userId = firebaseAuth.currentUser!!.uid

            viewModel.saveAccount(
                AccountModel(accountName, balance.toDouble(), selectedWallet.id, userId)
            )
        }

        return binding.root
    }

    private fun isValid(): Boolean {
        val selectedWallet = binding.walletSp.selectedItem as WalletModel
        val accountName = binding.accountNameEt.text
        val balance = binding.balanceNameEt.text
        var isValid = true

        if(selectedWallet.id.isEmpty()) isValid = false

        if(accountName.isNullOrEmpty()){
            binding.accountNameEt.error = "Account name is required"
            isValid = false
        }

        if(balance.isNullOrEmpty()){
            isValid = false
            binding.balanceNameEt.error = "Balance is required"
        }

        return isValid
    }

    private fun setSpinnerLabel() {
        if (walletType == WalletType.Banks) {
            binding.walletLabelTv.text = "Chose bank"
        } else if (walletType == WalletType.Crypto) {
            binding.walletLabelTv.text = "Chose crypto"
        }
    }

    private fun setupObservers(){
        viewModel.walletModelListLiveData.observe(viewLifecycleOwner) {
            when(it.resultType){
                ResultType.Loading -> {
                    binding.pBar.visibility = View.VISIBLE
                }
                ResultType.Success -> {
                    setupSpinner(it.data!!)
                    binding.pBar.visibility = View.GONE
                }
                else -> binding.pBar.visibility = View.GONE
            }
        }

        viewModel.saveAccountResultLiveData.observe(viewLifecycleOwner) {
            when(it.resultType){
                ResultType.Success -> {
                    binding.savePBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_addAccountsFragment_to_AccountFragment)
                }
                ResultType.Error -> {
                    binding.savePBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error: " + it.message, Toast.LENGTH_SHORT).show()
                }
                ResultType.Loading -> binding.savePBar.visibility = View.VISIBLE
                else -> binding.savePBar.visibility = View.GONE
            }
        }
    }

    private fun setupSpinner(data: List<WalletModel>){
        val adapter = SpinnerAdapterWithIcon(this.requireContext(), R.layout.item_spinner,
            data.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.walletSp.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(walletType: String) =
            AddAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(WALLET_TYPE, walletType)
                }
            }
    }
}