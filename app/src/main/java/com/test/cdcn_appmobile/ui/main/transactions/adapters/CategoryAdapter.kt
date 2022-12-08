package com.test.cdcn_appmobile.ui.main.transactions.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.cdcn_appmobile.data.models.Category
import com.test.cdcn_appmobile.databinding.ItemExpenditureBinding
import com.test.cdcn_appmobile.utils.Constant

/*
 * Created by tuyen.dang on 12/3/2022
 */

class CategoryAdapter(
    private var idCategory: String,
    private var listData: MutableList<Category>,
    private val onItemClick: (Category) -> Unit,
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding =
            ItemExpenditureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int = listData.size

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(listData: MutableList<Category>) {
        this.listData = listData
        notifyDataSetChanged()
    }

    inner class CategoryHolder(private val binding: ItemExpenditureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        internal fun bindView(position: Int) {
            binding.run {

                Glide.with(binding.root).load(Constant.baseURL + listData[position].linkIcon)
                    .fitCenter()
                    .into(roundedImageView)

                tvName.text = listData[position].name
                tvName.isSelected = (listData[position].id == idCategory)

                layoutItem.run {
                    setOnClickListener {
                        onItemClick(listData[position])
                    }
                    isSelected = (listData[position].id == idCategory)
                }
            }
        }
    }

}
