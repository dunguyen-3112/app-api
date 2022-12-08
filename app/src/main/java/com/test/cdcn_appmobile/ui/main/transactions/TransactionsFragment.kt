package com.test.cdcn_appmobile.ui.main.transactions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.DrawerObject
import com.test.cdcn_appmobile.data.models.Expenditure
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.FragmentTransactionsBinding
import com.test.cdcn_appmobile.extension.*
import com.test.cdcn_appmobile.ui.dialog.ChoiceFragment
import com.test.cdcn_appmobile.ui.main.transactions.adapters.DayPickerAdapter
import com.test.cdcn_appmobile.ui.main.transactions.adapters.ExpenditureAdapter
import com.test.cdcn_appmobile.ui.main.transactions.detail.ExpenditureDetailFragment
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil
import java.util.*

/*
 * Created by tuyen.dang on 11/27/2022
 */

class TransactionsFragment : Fragment() {

    companion object {
        private var listDay: MutableList<ItemChoice> = ArrayList()
        private var listMonth: MutableList<ItemChoice> = ArrayList()
        private var listYear: MutableList<ItemChoice> = ArrayList()
    }

    private var binding: FragmentTransactionsBinding? = null
    private var transactionsViewModel: TransactionsViewModel? = null
    private var choiceFragment: ChoiceFragment? = null
    private var expenditureAdapter: ExpenditureAdapter? = null
    private var listExpenditure: MutableList<Expenditure> = ArrayList()
    private var dayPickerAdapter: DayPickerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        if (transactionsViewModel?.getListExpenditure()?.value == null) {
            loadData()
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun initView() {

        transactionsViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.transactionsViewModelFactory()
            )[TransactionsViewModel::class.java]


        binding?.run {

            transactionsViewModel?.run {

                expenditureAdapter = ExpenditureAdapter(listExpenditure) {
                    requireActivity().addFragment(
                        R.id.ctReplaceFragment,
                        ExpenditureDetailFragment(it, ::onCallReload),
                        true,
                        "detail"
                    )
                }

                dayPickerAdapter = DayPickerAdapter(listDay) {
                    setIdDayChosen(it.id)
                }

                getIdYearChosen().observe(viewLifecycleOwner) {
                    btnChoiceYear.text = it.toString()
                    listYear.OnItemChoice(it)
                }

                getIdMonthChosen().observe(viewLifecycleOwner) {
                    btnChoiceMonth.text = it.toString()
                    listMonth.OnItemChoice(it)
                    initDataDay(
                        it,
                        getIdYearChosen().value ?: Calendar.getInstance().get(Calendar.YEAR)
                    )
                }

                getIdDayChosen().observe(viewLifecycleOwner) {
                    listDay.OnItemChoice(it)
                    dayPickerAdapter?.notifyDataSetChanged()
                    loadData()
                }

                getListExpenditure().observe(viewLifecycleOwner) {
                    it?.let {
                        layoutGraph.removeAllViews()
                        listExpenditure.clear()
                        listExpenditure.addAll(it)
                        expenditureAdapter?.notifyDataSetChanged()

                        tvWarningEmpty.setVisibility(listExpenditure.isEmpty())

                        var allReceiver = 0L
                        var allSpent = 0L
                        for (i in listExpenditure) {
                            if (i.categoryType == 0) {
                                allReceiver += i.cost
                            } else {
                                allSpent += i.cost
                            }
                        }

                        tvMoneyLimited.text = "+${allReceiver.toStringNumber()}đ"
                        tvMoneySpent.text = "-${allSpent.toStringNumber()}đ"
                        tvBudget.run {
                            text =
                                "${if (allReceiver >= allSpent) "+" else ""}${(allReceiver - allSpent).toStringNumber()}đ"
                            isSelected = (allReceiver >= allSpent)
                        }
                        tvTotalDay.run {
                            text =
                                "${if (allReceiver >= allSpent) "+" else ""}${(allReceiver - allSpent).toStringNumber()}đ"
                            isSelected = (allReceiver >= allSpent)
                        }

                        val mutableList: MutableList<DrawerObject> = mutableListOf()
                        mutableList.add(DrawerObject(allSpent / 1000, allReceiver / 1000, ""))

                        val drawView = DrawView(requireContext(), mutableList)

                        layoutGraph.addView(drawView)

                    }
                }

                if (getIdYearChosen().value == null || getIdYearChosen().value == -1) {
                    setIdYearChosen(Calendar.getInstance().get(Calendar.YEAR))
                }
                if (getIdMonthChosen().value == null || getIdMonthChosen().value == -1) {
                    setIdMonthChosen(Calendar.getInstance().get(Calendar.MONTH) + 1)
                }
                if (getIdDayChosen().value == null || getIdDayChosen().value == -1) {
                    setIdDayChosen(Calendar.getInstance().get(Calendar.DATE))
                }
            }

            rclViewSpending.layoutManager = LinearLayoutManager(context)
            rclViewSpending.adapter = expenditureAdapter

            rclViewDay.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rclViewDay.adapter = dayPickerAdapter

            if (listYear.isEmpty() || listMonth.isEmpty()) {
                initDataMonthYear()
            }
            if (listDay.isEmpty()) {
                initDataDay(
                    transactionsViewModel?.getIdMonthChosen()?.value ?: (Calendar.getInstance()
                        .get(Calendar.MONTH) + 1),
                    transactionsViewModel?.getIdYearChosen()?.value ?: Calendar.getInstance()
                        .get(Calendar.YEAR)
                )
            }
        }

    }

    private fun initListener() {
        binding?.run {
            btnChoiceYear.setOnClickListener {
                openDialogChoice("Năm", listYear) { idChosen ->
                    transactionsViewModel?.setIdYearChosen(idChosen)
                }
            }

            btnChoiceMonth.setOnClickListener {
                openDialogChoice("Tháng", listMonth) { idChosen ->
                    transactionsViewModel?.setIdMonthChosen(idChosen)
                }
            }

            refreshLayout.setOnRefreshListener {
                loadData()
            }

            transactionsViewModel?.run {
                btnAddExpenditure.setOnClickListener {

                    requireActivity().addFragment(
                        R.id.ctReplaceFragment,
                        ExpenditureDetailFragment(
                            Expenditure("",
                                0,
                                "${
                                    getIdDayChosen().value?.toString()?.toDayMonth()
                                }-${
                                    getIdMonthChosen().value?.toString()?.toDayMonth()
                                }-${getIdYearChosen().value}",
                                "",
                                "",
                                "",
                                0,
                                "",
                                ""),
                            ::onCallReload),
                        true,
                        "detail"
                    )
                }
            }
        }
    }

    private fun initDataMonthYear() {
        listMonth.clear()
        listYear.clear()
        for (i in 1..12) {
            listMonth.add(ItemChoice(i, i.toString(), false))
        }
        for (i in 2010..Calendar.getInstance().get(Calendar.YEAR)) {
            listYear.add(ItemChoice(i, i.toString(), false))
        }
        listYear.reverse()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initDataDay(monthChosen: Int, yearChosen: Int) {
        listDay.clear()
        val day = when (monthChosen) {
            2 -> if (yearChosen % 4 == 0) 29 else 28
            1, 3, 5, 7, 8, 10, 12 -> 31
            else -> 30
        }
        for (i in 1..day) {
            listDay.add(ItemChoice(i,
                i.toString(),
                i == transactionsViewModel?.getIdDayChosen()?.value))
        }
        transactionsViewModel?.setIdDayChosen(transactionsViewModel?.getIdDayChosen()?.value ?: 0)
        binding?.run {
            rclViewDay.scrollToPosition((transactionsViewModel?.getIdDayChosen()?.value ?: 1) - 1)
        }
        dayPickerAdapter?.notifyDataSetChanged()
    }

    private fun openDialogChoice(
        title: String,
        listData: MutableList<ItemChoice>,
        onItemChoice: (idChosen: Int) -> Unit,
    ) {
        choiceFragment = ChoiceFragment(
            list_data = listData,
            title,
            itemListener = {
                onItemChoice(it.id)
                choiceFragment?.dismiss()
            },
            onChoiceFragHide = {

            })
        choiceFragment?.show(requireActivity().supportFragmentManager, choiceFragment?.tag ?: "")
    }

    private fun loadData() {
        transactionsViewModel?.run {
            binding?.run {
                refreshLayout.isEnabled = false
                viewBg.setVisibility(true)
                progressBar.setVisibility(true)
                getListExpenditureFromServer(
                    resources.getString(
                        R.string.tokenJWT,
                        Constant.USER.tokenJWT
                    ),
                    Constant.USER.id
                ) { message ->
                    refreshLayout.isEnabled = true
                    refreshLayout.isRefreshing = false
                    viewBg.setVisibility(false)
                    progressBar.setVisibility(false)
                    message?.let {
                        context?.let {
                            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    private fun onCallReload(day: Int, month: Int, year: Int) {

        requireActivity().backToPreFragment()
        transactionsViewModel?.run {
            setIdYearChosen(year)
            setIdMonthChosen(month)
            setIdDayChosen(day)
        }
    }

}
