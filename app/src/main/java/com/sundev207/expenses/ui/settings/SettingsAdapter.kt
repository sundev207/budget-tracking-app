package com.sundev207.expenses.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sundev207.expenses.R

private const val DEFAULT_CURRENCY_ITEM_TYPE = R.layout.item_default_currency
private const val GENERAL_HEADER_TYPE = R.layout.header_general

class SettingsAdapter : ListAdapter<SettingItemModel, SettingItemHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            DEFAULT_CURRENCY_ITEM_TYPE -> DefaultCurrencyItemHolder(itemView)
            GENERAL_HEADER_TYPE -> GeneralHeaderHolder(itemView)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: SettingItemHolder, position: Int) {
        val itemModel = getItem(position)
        when {
            holder is DefaultCurrencyItemHolder && itemModel is DefaultCurrencyItemModel ->
                holder.bind(itemModel)
        }
    }

    override fun onViewRecycled(holder: SettingItemHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is DefaultCurrencyItemHolder -> holder.recycle()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DefaultCurrencyItemModel -> DEFAULT_CURRENCY_ITEM_TYPE
            is GeneralHeaderModel -> GENERAL_HEADER_TYPE
            else -> super.getItemViewType(position)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<SettingItemModel>() {

        override fun areItemsTheSame(
                oldItem: SettingItemModel,
                newItem: SettingItemModel
        ): Boolean {
            return when {
                oldItem is DefaultCurrencyItemModel && newItem is DefaultCurrencyItemModel ->
                    oldItem.currency.code == newItem.currency.code
                oldItem is GeneralHeaderModel && newItem is GeneralHeaderModel -> true
                else -> false
            }
        }

        override fun areContentsTheSame(
                oldItem: SettingItemModel,
                newItem: SettingItemModel
        ): Boolean {
            return when {
                oldItem is DefaultCurrencyItemModel && newItem is DefaultCurrencyItemModel ->
                    oldItem.currency == newItem.currency
                oldItem is GeneralHeaderModel && newItem is GeneralHeaderModel -> true
                else -> false
            }
        }
    }
}