package com.test.cdcn_appmobile.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.databinding.FragmentHomeBinding
import com.test.cdcn_appmobile.extension.replaceFragment
import com.test.cdcn_appmobile.ui.main.budget.BudgetFragment
import com.test.cdcn_appmobile.ui.main.settings.SettingsFragment
import com.test.cdcn_appmobile.ui.main.statistical.StatisticalFragment
import com.test.cdcn_appmobile.ui.main.transactions.TransactionsFragment

/*
 * Created by tuyen.dang on 11/27/2022
 */

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        requireActivity().replaceFragment(
            R.id.ctFragmentUser,
            BudgetFragment()
        )

        requireActivity().replaceFragment(
            R.id.ctFragmentUser,
            TransactionsFragment()
        )

        binding?.run {

        }
    }

    private fun initListener() {
        binding?.run {
            bottomNavigation.setOnItemSelectedListener {
                var fragment: Fragment? = null
                when (it.itemId) {
                    R.id.transactionMenu -> {
                        fragment = TransactionsFragment()
                    }
                    R.id.budgetMenu -> {
                        fragment = BudgetFragment()
                    }
                    R.id.statisticalMenu -> {
                        fragment = StatisticalFragment()
                    }
                    R.id.settingMenu -> {
                        fragment = SettingsFragment()
                    }
                    else -> {
                    }
                }
                fragment?.let { result ->
                    requireActivity().replaceFragment(
                        R.id.ctFragmentUser,
                        result
                    )
                }
                true
            }

        }

    }

}
