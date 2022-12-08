package com.test.cdcn_appmobile.ui.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.FeatureListItemBinding

internal class ListChoiceItemAdapter(
    private var list_data: MutableList<ItemChoice>,
    private val itemChoiceListener: (item: ItemChoice) -> Unit
) : RecyclerView.Adapter<ListChoiceItemAdapter.ListChoiceHolder?>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(list_data: MutableList<ItemChoice>) {
        this.list_data = list_data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListChoiceHolder {
        val binding =
            FeatureListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListChoiceHolder(binding)
    }

    override fun onBindViewHolder(holder: ListChoiceHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int = list_data.size


    internal inner class ListChoiceHolder(private val binding: FeatureListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(position: Int) {
            binding.run {
                txtNameItem.text = list_data[position].name
                cbItem.isChecked = list_data[position].isCheck
                layoutItemChoice.setOnClickListener {
                    itemChoiceListener(list_data[position])
                    cbItem.isChecked = list_data[position].isCheck
                }
                cbItem.setOnClickListener {
                    itemChoiceListener(
                        list_data[position]
                    )
                }
            }
        }
    }
}