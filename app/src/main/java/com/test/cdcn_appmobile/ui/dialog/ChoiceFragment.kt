package com.test.cdcn_appmobile.ui.dialog

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.FragmentListChoiceBinding
import java.util.*

class ChoiceFragment(
    private var list_data: MutableList<ItemChoice>,
    private var title: String,
    private var itemListener: (ItemChoice) -> Unit,
    private var onChoiceFragHide: () -> Unit
) :
    BottomSheetDialogFragment() {

    private var binding: FragmentListChoiceBinding? = null
    private var mBehavior: BottomSheetBehavior<*>? = null
    private var adapter: ListChoiceItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListChoiceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
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

    private fun setUp() {
        binding?.run {
            layoutMainChoice.layoutParams?.run {
                val currentOrientation = resources.configuration.orientation

                height = requireActivity().resources.displayMetrics.heightPixels * 7 / 10
                if (currentOrientation != Configuration.ORIENTATION_LANDSCAPE)
                    width = requireActivity().resources.displayMetrics.widthPixels
            }
            txtTileOption.text = title
            btnCancelChoice.setOnClickListener { dismiss() }
            adapter = ListChoiceItemAdapter(list_data, itemListener)
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
        }
    }

    private fun search(text: String) {
        val listSearch: ArrayList<ItemChoice> = ArrayList<ItemChoice>()
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

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
