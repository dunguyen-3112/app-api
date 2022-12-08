package com.test.cdcn_appmobile.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.Category
import com.test.cdcn_appmobile.databinding.FragmentListChoiceBinding
import com.test.cdcn_appmobile.extension.setVisibility
import com.test.cdcn_appmobile.ui.main.transactions.adapters.CategoryAdapter
import com.test.cdcn_appmobile.ui.main.transactions.detail.ExDetailViewModel
import com.test.cdcn_appmobile.utils.Constant
import com.test.cdcn_appmobile.utils.InjectorUtil
import java.util.*

/*
 * Created by tuyen.dang on 12/3/2022
 */

class ChoiceCategoryFragment(
    private var idCategory: String,
    private var idCategoryType: Int,
    private var title: String,
    private var itemListener: (Category) -> Unit,
    private var onChoiceFragHide: () -> Unit,
) : BottomSheetDialogFragment() {

    private var binding: FragmentListChoiceBinding? = null
    private var mBehavior: BottomSheetBehavior<*>? = null
    private var adapter: CategoryAdapter? = null
    private var list_data: MutableList<Category> = mutableListOf()
    private var viewModel: ExDetailViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentListChoiceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        loadData()
    }

    override fun onDestroyView() {
        onChoiceFragHide()
        super.onDestroyView()
        binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.fragment_list_choice, null)
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUp() {
        viewModel =
            ViewModelProvider(
                requireActivity(),
                InjectorUtil.exDetailViewModelFactory()
            )[ExDetailViewModel::class.java]

        binding?.run {
            layoutMainChoice.layoutParams?.run {
                height = requireActivity().resources.displayMetrics.heightPixels * 7 / 10
                width = requireActivity().resources.displayMetrics.widthPixels
            }

            txtTileOption.text = title

            btnCancelChoice.setOnClickListener { dismiss() }
            adapter = CategoryAdapter(idCategory, list_data, itemListener)
            rclChoice.layoutManager = LinearLayoutManager(activity)
            rclChoice.adapter = adapter

            searchViewChoice.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    search(s)
                    if (s.isEmpty()) {
                        adapter?.setListData(list_data)
                    }
                    return false
                }
            })

            viewModel?.run {
                getListCategory().observe(viewLifecycleOwner) {
                    list_data.clear()
                    list_data.addAll(it ?: mutableListOf())
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun search(text: String) {
        val listSearch: ArrayList<Category> = ArrayList<Category>()
        for (i in list_data) {
            if (i.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) listSearch.add(i)
        }
        if (listSearch.isEmpty()) {
            if (text.isNotEmpty())
                context?.let {
                    Toast.makeText(it, "Không tìm thấy dữ liệu !!!", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            adapter?.setListData(listSearch)
        }
    }

    private fun loadData() {
        viewModel?.run {
            binding?.run {
                viewBg.setVisibility(true)
                progressBar.setVisibility(true)
                getListCategoryFromServer(
                    resources.getString(
                        R.string.tokenJWT,
                        Constant.USER.tokenJWT
                    ),
                    idCategoryType
                ) { message ->
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

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
