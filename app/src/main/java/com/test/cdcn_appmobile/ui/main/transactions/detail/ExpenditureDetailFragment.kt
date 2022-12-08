package com.test.cdcn_appmobile.ui.main.transactions.detail

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.Expenditure
import com.test.cdcn_appmobile.databinding.FragmentDetailExpenditureBinding
import com.test.cdcn_appmobile.extension.*
import com.test.cdcn_appmobile.ui.dialog.ChoiceCategoryFragment
import com.test.cdcn_appmobile.ui.dialog.DatePickerFragment
import com.test.cdcn_appmobile.ui.main.budget.BudgetViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil


/*
 * Created by tuyen.dang on 12/2/2022
 */

class ExpenditureDetailFragment(
    private val expenditure: Expenditure,
    private val onSuccess: (Int, Int, Int) -> Unit,
) : Fragment() {

    private var binding: FragmentDetailExpenditureBinding? = null
    private var viewModel: ExDetailViewModel? = null
    private var choiceCategoryFragment: ChoiceCategoryFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailExpenditureBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {

        viewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.exDetailViewModelFactory()
            )[ExDetailViewModel::class.java]

        binding?.run {
            viewModel?.run {
                setIdCategory(expenditure.categoryId)

                getExpenditureDetail().observe(viewLifecycleOwner) { ex ->
                    edtCost.text = Editable.Factory.getInstance()
                        .newEditable(ex?.cost?.toStringNumber())

                    setCategoryType(ex?.categoryType ?: 0)

                    setIdCategory(ex?.categoryId ?: "")

                    tvCategory.text = ex?.categoryName

                    Glide.with(root).load(Constant.baseURL + ex?.imageIcon).into(imgCategory)

                    edtNote.text = Editable.Factory.getInstance()
                        .newEditable(ex?.note)

                    tvDayChoice.text = if (ex?.date == "") getDay() else ex?.date

                    btnDel.setVisibility(ex?.id != "")

                    btnSave.text =
                        if (ex?.id == "") resources.getString(R.string.add) else resources.getString(
                            R.string.update
                        )


                    if (ex?.categoryType == 0) {
                        radioReceiver.isChecked = true
                    } else {
                        radioSpent.isChecked = true
                    }
                }

                getIsEditing().observe(viewLifecycleOwner) {
                    edtCost.isEnabled = it
                    btnEditCost.isSelected = it
                    edtCost.run {
                        filters = arrayOf<InputFilter>(LengthFilter(if (it) 11 else 14))

                        text =
                            if (it)
                                Editable.Factory.getInstance()
                                    .newEditable(edtCost.text.toString().replace(",", ""))
                            else Editable.Factory.getInstance()
                                .newEditable(
                                    edtCost.text.toString().replace(",", "").toLong()
                                        .toStringNumber()
                                )
                    }
                }

            }
        }

        viewModel?.setExpenditureDetail(this.expenditure)
    }

    private fun initListener() {
        binding?.run {
            btnBack.setOnClickListener {
                requireActivity().backToPreFragment()
            }

            viewModel?.run {
                btnEditCost.setOnClickListener {
                    changeEditing()
                }

                btnChoiceCategory.setOnClickListener {
                    choiceCategoryFragment = ChoiceCategoryFragment(
                        getIdCategory().value ?: "",
                        getCategoryType().value ?: 0,
                        if (getCategoryType().value == 0) "Thu Nhập" else "Chi Tiêu",
                        itemListener = {
                            setIdCategory(it.id)
                            tvCategory.text = it.name
                            Glide.with(root).load(Constant.baseURL + it.linkIcon).into(imgCategory)
                            choiceCategoryFragment?.dismiss()
                        },
                        onChoiceFragHide = {

                        })
                    choiceCategoryFragment?.show(
                        requireActivity().supportFragmentManager,
                        choiceCategoryFragment?.tag ?: ""
                    )
                }

                radioSpent.setOnClickListener {
                    setCategoryType(1)
                    imgCategory.setImageResource(R.drawable.bg_category_choice)
                    tvCategory.text = ""
                    setIdCategory("")
                }

                radioReceiver.setOnClickListener {
                    setCategoryType(0)
                    imgCategory.setImageResource(R.drawable.bg_category_choice)
                    tvCategory.text = ""
                    setIdCategory("")
                }

                btnDel.setOnClickListener {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

                    builder.setMessage("Bạn có chắc muốn xóa chi tiêu ?")

                    builder.setPositiveButton(
                        "Có"
                    ) { dialog, _ ->
                        viewBg.setVisibility(true)
                        progressBar.setVisibility(true)
                        deleteExpenditure(
                            resources.getString(
                                R.string.tokenJWT,
                                Constant.USER.tokenJWT
                            ),
                            expenditure.id
                        ) { done, message ->
                            BudgetViewModel.changeNewExpenditure()
                            dialog.dismiss()
                            context?.let {
                                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                            }
                            if (done) {
                                viewBg.setVisibility(false)
                                progressBar.setVisibility(false)
                                val time = tvDayChoice.text.toString().split("-")
                                onSuccess(time[0].toInt(), time[1].toInt(), time[2].toInt())
                            }
                        }
                    }

                    builder.setNegativeButton(
                        "Không"
                    ) { dialog, _ -> dialog.dismiss() }
                    builder.show()
                }

                btnSave.setOnClickListener {
                    expenditure.apply {
                        cost = edtCost.text.toString().replace(",", "").toLong()
                        date = tvDayChoice.text.toString().trim()
                        note = edtNote.text.toString()
                        categoryId = getIdCategory().value ?: ""
                    }

                    if (getIsEditing().value == false
                        && getIdCategory().value != ""
                        && edtCost.text.toString()
                            .replace(",", "").toLong() > 0
                    ) {
                        if (getCategoryType().value == 1
                            && (BudgetViewModel.budget.value?.remainMoney
                                ?: Long.MAX_VALUE) < expenditure.cost
                        ) {
                            requireActivity().showDialogFrag {
                                viewBg.setVisibility(true)
                                progressBar.setVisibility(true)
                                addOrUpdateExpenditure(
                                    resources.getString(
                                        R.string.tokenJWT,
                                        Constant.USER.tokenJWT
                                    ),
                                    Constant.USER.id,
                                    expenditure
                                ) { done, message ->
                                    BudgetViewModel.changeNewExpenditure()
                                    viewBg.setVisibility(false)
                                    progressBar.setVisibility(false)
                                    context?.let {
                                        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                    }
                                    if (done) {
                                        val time = tvDayChoice.text.toString().split("-")
                                        onSuccess(time[0].toInt(), time[1].toInt(), time[2].toInt())
                                    }
                                }
                            }
                        } else {

                            viewBg.setVisibility(true)
                            progressBar.setVisibility(true)
                            addOrUpdateExpenditure(
                                resources.getString(
                                    R.string.tokenJWT,
                                    Constant.USER.tokenJWT
                                ),
                                Constant.USER.id,
                                expenditure
                            ) { done, message ->
                                BudgetViewModel.changeNewExpenditure()
                                viewBg.setVisibility(false)
                                progressBar.setVisibility(false)
                                context?.let {
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                                if (done) {
                                    val time = tvDayChoice.text.toString().split("-")
                                    onSuccess(time[0].toInt(), time[1].toInt(), time[2].toInt())
                                }
                            }
                        }

                    } else {
                        context?.let {
                            Toast.makeText(
                                it,
                                "Vui lòng xác nhận lượng tiền thu chi và chọn thể loại !!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            btnChoiceDay.setOnClickListener {
                val newFragment: DialogFragment = DatePickerFragment { result ->
                    tvDayChoice.text = result
                }
                activity?.let { newFragment.show(it.supportFragmentManager, "datePicker") }
            }

        }
    }

}
