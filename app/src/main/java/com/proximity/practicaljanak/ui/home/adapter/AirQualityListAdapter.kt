package com.proximity.practicaljanak.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.proximity.practicaljanak.base.BaseAdapterWithViewBinding
import com.proximity.practicaljanak.databinding.ItemAirQualityListBinding
import com.proximity.practicaljanak.extensions.*
import com.proximity.practicaljanak.interfaces.OnRecyclerViewItemClicked
import com.proximity.practicaljanak.models.AirQualityData

class AirQualityListAdapter(
    private val airQualityDataList: ArrayList<AirQualityData>, private val onItemClick: OnRecyclerViewItemClicked
) : BaseAdapterWithViewBinding(airQualityDataList) {

    override fun getViewBinding(viewType: Int, parent: ViewGroup): ViewBinding {
        return ItemAirQualityListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val context = holder.binding.root.context
        val airQualityListDataBean = airQualityDataList[position]

        val binding = holder.binding
        if (binding is ItemAirQualityListBinding) {
            binding.tvCityName.setCityName(airQualityListDataBean.city.nullSafe())
            binding.tvCurrentAQI.setAQI(airQualityListDataBean.aqi.nullSafe().getFormattedDecimalNumber())
            binding.tvQuality.setQuality(airQualityListDataBean.aqi.nullSafe())
        }

        holder.itemView.rootView.setOnClickListener {
            onItemClick.onItemViewClicked(holder.adapterPosition)
        }
    }
}