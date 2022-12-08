package com.test.cdcn_appmobile.ui.main.statistical

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.databinding.FragmentStatisticalBinding
import com.test.cdcn_appmobile.ui.main.statistical.graph.GraphActivity
import com.test.cdcn_appmobile.utils.InjectorUtil

/*
 * Created by tuyen.dang on 11/28/2022
 */

class StatisticalFragment : Fragment() {

    private var binding: FragmentStatisticalBinding? = null
    private var statisticalViewModel: StatisticalViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatisticalBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        statisticalViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.statisticalViewModelFactory()
            )[StatisticalViewModel::class.java]

        binding?.run {

        }
    }

    private fun initListener() {
        binding?.run {
            btnByDay.setOnClickListener {
                val intent = Intent(requireActivity(), GraphActivity::class.java)
                intent.putExtra("forDayMonth", false)
                startActivity(intent)
            }

            btnByYear.setOnClickListener {
                val intent = Intent(requireActivity(), GraphActivity::class.java)
                intent.putExtra("forDayMonth", true)
                startActivity(intent)
            }
        }
    }
}
