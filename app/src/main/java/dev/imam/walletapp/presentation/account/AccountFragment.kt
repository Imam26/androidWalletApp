package dev.imam.walletapp.presentation.account

import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dev.imam.walletapp.R
import dev.imam.walletapp.databinding.FragmentAccountBinding
import dev.imam.walletapp.presentation.models.BalanceModel
import dev.imam.walletapp.presentation.models.ResultType
import dev.imam.walletapp.presentation.models.WalletModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    private val viewModel: AccountViewModel by viewModel()

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var walletAdapter: WalletAdapter
    private lateinit var balanceAdapter: BalancePagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private var walletTypeIds = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)

        setupToolbar()
        setupTabLayoutWithViewPager()
        setupRecyclerView()
        setupLiveDataObservers()

        firebaseAuth = FirebaseAuth.getInstance()

        viewModel.loadWalletList(firebaseAuth.currentUser!!.uid)

        return binding.root
    }

    private fun setupLiveDataObservers(){
        viewModel.walletModelListLiveData.observe(this.viewLifecycleOwner) {
            walletAdapter.setItems(it)
        }

        viewModel.balanceModelResultLiveData.observe(this.viewLifecycleOwner) {
            when(it.resultType){
                ResultType.Loading -> {
                    startState()
                }
                ResultType.Error -> {
                    errorState()
                    Toast.makeText(requireContext(), "Error: " + it.message, Toast.LENGTH_SHORT).show()
                }
                ResultType.Success -> {
                    if(it.data.isNullOrEmpty()){
                        noDataState()
                    } else {
                        dataExistState()
                        walletTypeIds = it.data!!.map { item -> item.id }.toMutableList()
                        balanceAdapter.setBalance(it.data!!)
                    }
                }
                else -> false
            }
        }

        viewModel.titlesLiveModel.observe(this.viewLifecycleOwner) {
            if(tabLayoutMediator.isAttached) {
                tabLayoutMediator.detach()
            }
            tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = it[position]
            }
            tabLayoutMediator.attach()
        }
    }

    private fun setupToolbar() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.account_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settingsMenuItem -> {
                        Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_accountsFragment_to_walletTypeFragment)
        }
    }

    private fun setupTabLayoutWithViewPager() {
        balanceAdapter = BalancePagerAdapter()

        binding.viewPager.adapter = balanceAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.sortWalletModels(walletTypeIds[position])
            }
        })

        tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, _->
            tab.text = ""
        }
        tabLayoutMediator.attach()
    }

    private fun setupRecyclerView(){
        walletAdapter = WalletAdapter {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }

        val balanceLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.currencyRecyclerView.apply {
            adapter = walletAdapter
            layoutManager = balanceLayoutManager
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        }
    }

    private fun noDataState() {
        binding.notDataTv.visibility = View.VISIBLE
        binding.frameLayout.visibility = View.GONE
        binding.currencyRecyclerView.visibility = View.GONE
        binding.pBar.visibility = View.GONE
    }

    private fun dataExistState(){
        binding.frameLayout.visibility = View.VISIBLE
        binding.currencyRecyclerView.visibility = View.VISIBLE
        binding.notDataTv.visibility = View.GONE
        binding.pBar.visibility = View.GONE
    }

    private fun startState(){
        binding.notDataTv.visibility = View.GONE
        binding.frameLayout.visibility = View.GONE
        binding.currencyRecyclerView.visibility = View.GONE
        binding.pBar.visibility = View.VISIBLE
    }

    private fun errorState(){
        binding.pBar.visibility = View.GONE
        binding.notDataTv.visibility = View.GONE
    }
}