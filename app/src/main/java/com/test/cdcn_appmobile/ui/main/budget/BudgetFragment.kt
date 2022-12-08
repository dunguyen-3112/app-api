package com.test.cdcn_appmobile.ui.main.budget

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.FragmentBudgetBinding
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.extension.toStringNumber
import com.test.cdcn_appmobile.ui.dialog.ChoiceFragment
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil

/*
 * Created by tuyen.dang on 11/28/2022
 */

class BudgetFragment : Fragment() {

    private var binding: FragmentBudgetBinding? = null
    private var budgetViewModel: BudgetViewModel? = null
    private var listUnitLoop: ArrayList<ItemChoice> = ArrayList()
    private var choiceFragment: ChoiceFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        if (budgetViewModel?.getBudget()?.value == null) {
            initData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        budgetViewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.provideBudgetViewModelFactory()
            )[BudgetViewModel::class.java]
        binding?.run {

            budgetViewModel?.run {
                getBudget().observe(viewLifecycleOwner) { budget ->
                    budget?.let {

                        tvWarningBudget.setVisibility(it.remainMoney < 0)
                        imgWarningBudget.setVisibility(it.remainMoney < 0)

                        edtLimited.text =
                            Editable.Factory.getInstance()
                                .newEditable(it.limitedMoney.toStringNumber())

                        setIdUnitChosen(it.unitTime)

                        tvMoneyLimited.text = "${it.limitedMoney.toStringNumber()} đ"
                        tvMoneySpent.text = "- ${it.spendMoney.toStringNumber()} đ"
                        tvBudget.run {
                            text = "${it.remainMoney.toStringNumber()} đ"
                            isSelected = it.remainMoney >= 0
                        }
                    }
                }

                getIsEditing().observe(viewLifecycleOwner) {
                    edtLimited.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(if (it) 11 else 14))
                    btnEnableEdit.isSelected = it
                    edtLimited.isEnabled = it
                    tvLoopUnit.isEnabled = it
                    btnLoopUnit.isEnabled = it
                }

                getIdUnitChosen().observe(viewLifecycleOwner) {
                    listUnitLoop.clear()
                    listUnitLoop.add(
                        ItemChoice(
                            0,
                            resources.getString(R.string.daily),
                            it == 0
                        )
                    )
                    listUnitLoop.add(
                        ItemChoice(
                            1,
                            resources.getString(R.string.monthly),
                            it == 1
                        )
                    )
                    listUnitLoop.add(
                        ItemChoice(
                            2,
                            resources.getString(R.string.yearly),
                            it == 2
                        )
                    )

                    tvLoopUnit.text = listUnitLoop[it].name
                }

                getIsNewExpenditure().observe(viewLifecycleOwner) {
                    viewBg.setVisibility(true)
                    progressBar.setVisibility(true)
                    getBudgetFromServer(
                        resources.getString(
                            R.string.tokenJWT,
                            Constant.USER.tokenJWT
                        ), Constant.USER.id
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

    }

    private fun initListener() {
        binding?.run {

            btnEnableEdit.setOnClickListener {
                budgetViewModel?.run {
                    if (getIsEditing().value == true) {
                        if (edtLimited.text.toString().trim().isNotEmpty()) {
                            viewBg.setVisibility(true)
                            progressBar.setVisibility(true)
                            updateBudget(
                                resources.getString(
                                    R.string.tokenJWT,
                                    Constant.USER.tokenJWT
                                ), Constant.USER.id, edtLimited.text.toString().trim()
                            ) { message ->
                                viewBg.setVisibility(false)
                                progressBar.setVisibility(false)
                                context?.let {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            context?.let {
                                Toast.makeText(
                                    it,
                                    "Vui lòng nhập hạn mức trước khi cập nhật !!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@setOnClickListener
                        }
                    } else {
                        edtLimited.text =
                            Editable.Factory.getInstance()
                                .newEditable(edtLimited.text.toString().replace(",", ""))
                    }
                    changeEditing()
                }
            }

            tvLoopUnit.setOnClickListener {
                openDialogChoice()
            }

            btnLoopUnit.setOnClickListener {
                openDialogChoice()
            }

            refreshLayout.setOnRefreshListener {
                initData()
            }
        }
    }

    private fun initData() {
        binding?.run {
            refreshLayout.isEnabled = false
            viewBg.setVisibility(true)
            progressBar.setVisibility(true)
            budgetViewModel?.getBudgetFromServer(
                resources.getString(
                    R.string.tokenJWT,
                    Constant.USER.tokenJWT
                ), Constant.USER.id
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

    private fun openDialogChoice() {
        choiceFragment = ChoiceFragment(
            list_data = listUnitLoop,
            resources.getString(R.string.loop),
            itemListener = {
                budgetViewModel?.setIdUnitChosen(it.id)
                choiceFragment?.dismiss()
            },
            onChoiceFragHide = {

            })
        choiceFragment?.show(requireActivity().supportFragmentManager, choiceFragment?.tag ?: "")
    }

}
