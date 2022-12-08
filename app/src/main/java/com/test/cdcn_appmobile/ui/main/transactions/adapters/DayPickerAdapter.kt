package com.test.cdcn_appmobile.ui.main.transactions.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.cdcn_appmobile.data.models.ItemChoice
import com.test.cdcn_appmobile.databinding.ItemDayPickerBinding

class DayPickerAdapter(
    private val listData: MutableList<ItemChoice>,
    private val onItemClick: (ItemChoice) -> Unit
) : RecyclerView.Adapter<DayPickerAdapter.DayPickerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayPickerHolder {
        val binding =
            ItemDayPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayPickerHolder(binding)
    }

    override fun onBindViewHolder(holder: DayPickerHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int = listData.size

    inner class DayPickerHolder(private val binding: ItemDayPickerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        internal fun bindView(position: Int) {
            binding.run {
                tvDay.text = listData[position].name
                tvDay.isSelected = (listData[position].isCheck)
                layoutItem.isSelected = (listData[position].isCheck)

                layoutItem.setOnClickListener {
                    onItemClick(listData[position])
                }
            }
        }
    }
}