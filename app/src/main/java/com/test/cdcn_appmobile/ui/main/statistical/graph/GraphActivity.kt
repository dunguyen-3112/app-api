package com.test.cdcn_appmobile.ui.main.statistical.graph

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.ActivityGraphBinding
import com.test.cdcn_appmobile.extension.OnItemChoice
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.extension.toSingleDay
import com.test.cdcn_appmobile.ui.dialog.ChoiceFragment
import com.test.cdcn_appmobile.ui.main.statistical.StatisticalViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil
import java.util.*

/*
 * Created by tuyen.dang on 12/4/2022
 */

class GraphActivity : AppCompatActivity() {

    companion object {
        private var listDayEnd: MutableList<ItemChoice> = ArrayList()
        private var listDayStart: MutableList<ItemChoice> = ArrayList()
        private var listMonth: MutableList<ItemChoice> = ArrayList()
        private var listYear: MutableList<ItemChoice> = ArrayList()
    }

    private var binding: ActivityGraphBinding? = null
    private var statisticalViewModel: StatisticalViewModel? = null
    private var choiceFragment: ChoiceFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        statisticalViewModel =
            ViewModelProvider(
                this@GraphActivity,
                InjectorUtil.statisticalViewModelFactory()
            )[StatisticalViewModel::class.java]

        binding?.run {

            statisticalViewModel?.run {

                getIdYearChosen().observe(this@GraphActivity) {
                    btnChoiceYearGraph.text = it.toString()
                    listYear.OnItemChoice(it)
                    if (getForDayMonth().value == true) {
                        loadData()
                    }
                }

                getIdMonthChosen().observe(this@GraphActivity) {
                    btnChoiceMonthGraph.text = it.toString()
                    listMonth.OnItemChoice(it)
                    initDataDayStart(
                        it,
                        getIdYearChosen().value ?: Calendar.getInstance().get(Calendar.YEAR)
                    )
                }

                getIdDayStart().observe(this@GraphActivity) {
                    btnChoiceStarDay.text = it.toString()
                    listDayStart.OnItemChoice(it)
                    initDataDayEnd(
                        getIdMonthChosen().value ?: (Calendar.getInstance()
                            .get(Calendar.MONTH) + 1),
                        getIdYearChosen().value ?: Calendar.getInstance().get(Calendar.YEAR),
                        it
                    )
                }

                getIdDayEnd().observe(this@GraphActivity) {
                    btnChoiceEndDay.text = it.toString()
                    listDayEnd.OnItemChoice(it)
                    loadData()
                }

                getForDayMonth().observe(this@GraphActivity) {
                    tvTitle.text =
                        if (it) "Đồ thị biểu diễn chi tiêu theo tháng" else "Đồ thị biểu diễn chi tiêu theo ngày"
                    tvUnitMoney.text = "Ngàn đồng"
                    if (it) {
                        btnChoiceMonthGraph.run {
                            isEnabled = !it
                            text = ""
                        }
                        btnChoiceStarDay.run {
                            isEnabled = !it
                            text = ""
                        }
                        btnChoiceEndDay.run {
                            isEnabled = !it
                            text = ""
                        }
                    }
                }

                getListDrawerObject().observe(this@GraphActivity) {
                    layoutDrawGraph.removeAllViews()
                    for (i in it) {
                        i.time =
                            if (getForDayMonth().value == true) "Tháng ${i.time}" else
                                "Ngày ${i.time.split("-")[0].toSingleDay()}"
                        i.receivedMoney /= 1_000
                        i.spendMoney /= 1_000
                    }

                    val drawView = DrawView(this@GraphActivity, it) {
                        val tv = TextView(this@GraphActivity)
                        layoutDrawGraph.addView(tv)
                        layoutDrawGraph.removeView(tv)
                        layoutDrawGraph.invalidate()
                    }
                    layoutDrawGraph.addView(drawView)
                }

                setForDayMonth(intent.getBooleanExtra("forDayMonth", false))

                if (getIdYearChosen().value == null || getIdYearChosen().value == -1) {
                    setIdYearChosen(Calendar.getInstance().get(Calendar.YEAR))
                }
                if ((getIdMonthChosen().value == null || getIdMonthChosen().value == -1)
                    && getForDayMonth().value == false
                ) {
                    setIdMonthChosen(Calendar.getInstance().get(Calendar.MONTH) + 1)
                }
                if ((getIdDayStart().value == null || getIdDayStart().value == -1)
                    && getForDayMonth().value == false
                ) {
                    setIdDayStart(Calendar.getInstance().get(Calendar.DATE))
                }
                if ((getIdDayEnd().value == null || getIdDayEnd().value == -1)
                    && getForDayMonth().value == false
                ) {
                    setIdDayEnd(Calendar.getInstance().get(Calendar.DATE))
                }
            }

        }

        if (listYear.isEmpty() || listMonth.isEmpty()) {
            initDataMonthYear()
        }
        if (listDayStart.isEmpty()) {
            initDataDayStart(
                statisticalViewModel?.getIdMonthChosen()?.value ?: (Calendar.getInstance()
                    .get(Calendar.MONTH) + 1),
                statisticalViewModel?.getIdYearChosen()?.value ?: Calendar.getInstance()
                    .get(Calendar.YEAR)
            )
        }
        if (listDayEnd.isEmpty()) {
            initDataDayEnd(
                statisticalViewModel?.getIdMonthChosen()?.value ?: (Calendar.getInstance()
                    .get(Calendar.MONTH) + 1),
                statisticalViewModel?.getIdYearChosen()?.value ?: Calendar.getInstance()
                    .get(Calendar.YEAR),
                statisticalViewModel?.getIdDayStart()?.value ?: Calendar.getInstance()
                    .get(Calendar.DATE)
            )
        }

    }

    private fun initListener() {
        binding?.run {
            btnBackGraph.setOnClickListener {
                finish()
            }

            btnChoiceYearGraph.setOnClickListener {
                openDialogChoice("Năm", listYear) { idChosen ->
                    statisticalViewModel?.setIdYearChosen(idChosen)
                }
            }

            btnChoiceMonthGraph.setOnClickListener {
                openDialogChoice("Năm", listMonth) { idChosen ->
                    statisticalViewModel?.setIdMonthChosen(idChosen)
                }
            }

            btnChoiceStarDay.setOnClickListener {
                openDialogChoice("Ngày bắt đầu", listDayStart) { idChosen ->
                    statisticalViewModel?.setIdDayStart(idChosen)
                }
            }

            btnChoiceEndDay.setOnClickListener {
                openDialogChoice("Ngày kết thúc", listDayEnd) { idChosen ->
                    statisticalViewModel?.setIdDayEnd(idChosen)
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

    private fun initDataDayStart(monthChosen: Int, yearChosen: Int) {
        listDayStart.clear()
        val day = when (monthChosen) {
            2 -> if (yearChosen % 4 == 0) 29 else 28
            1, 3, 5, 7, 8, 10, 12 -> 31
            else -> 30
        }

        for (i in 1..day) {
            listDayStart.add(
                ItemChoice(
                    i,
                    i.toString(),
                    i == statisticalViewModel?.getIdDayStart()?.value
                )
            )
        }

        statisticalViewModel?.setIdDayStart(statisticalViewModel?.getIdDayStart()?.value ?: 1)
    }

    private fun initDataDayEnd(monthChosen: Int, yearChosen: Int, idDayStart: Int) {
        listDayEnd.clear()
        val day = when (monthChosen) {
            2 -> if (yearChosen % 4 == 0) 29 else 28
            1, 3, 5, 7, 8, 10, 12 -> 31
            else -> 30
        }

        for (i in idDayStart..day) {
            listDayEnd.add(
                ItemChoice(
                    i,
                    i.toString(),
                    i == statisticalViewModel?.getIdDayEnd()?.value
                )
            )
        }

        statisticalViewModel?.setIdDayEnd(statisticalViewModel?.getIdDayEnd()?.value ?: idDayStart)
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
        choiceFragment?.show(supportFragmentManager, choiceFragment?.tag ?: "")
    }

    private fun loadData() {
        statisticalViewModel?.run {
            binding?.run {
                viewBg.setVisibility(true)
                progressBar.setVisibility(true)
                getDrawObjectFromServer(
                    resources.getString(
                        R.string.tokenJWT,
                        Constant.USER.tokenJWT
                    ),
                    Constant.USER.id
                ) { message ->
                    viewBg.setVisibility(false)
                    progressBar.setVisibility(false)
                    message?.let {
                        Toast.makeText(this@GraphActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}