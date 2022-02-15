package com.proximity.practicaljanak.ui.home.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.proximity.practicaljanak.R
import com.proximity.practicaljanak.base.BaseFragment
import com.proximity.practicaljanak.common.Resource
import com.proximity.practicaljanak.databinding.FragmentAirQualityDetailsBinding
import com.proximity.practicaljanak.extensions.*
import com.proximity.practicaljanak.models.AirQualityData
import com.proximity.practicaljanak.utils.CommonUtils.checkInternetConnected
import com.proximity.practicaljanak.utils.TIME_INTERVAL
import com.proximity.practicaljanak.viewmodels.HomeScreenViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

class AirQualityDetailsFragment :
    BaseFragment<HomeScreenViewModel, FragmentAirQualityDetailsBinding>() {

    override val modelClass: KClass<HomeScreenViewModel>
        get() = HomeScreenViewModel::class

    override fun getViewBinding(): FragmentAirQualityDetailsBinding =
        FragmentAirQualityDetailsBinding.inflate(layoutInflater)

    override fun isActivityScopeViewModel(): Boolean = true
    private val graphValues = ArrayList<Entry>()
    private var airQualityDataFromPreviousFragment: AirQualityData = AirQualityData()
    private val timer = Timer()
    private var isTimerFinish : Boolean = true

    private val fetchAirQualityListObserver = Observer<Any> {
        when (it) {
            is Resource.Error<*> -> {
                showToast(context = requireContext(), message = it.message.nullSafe())
            }

            is Resource.Success<*> -> {
                hideProgressDialog()
                val response = it.data as String
                if (response.isNotEmpty()) {
                    if (isTimerFinish){
                        isTimerFinish = false
                        setAQIDataInDetails(response)
                    }
                } else {
                    showToast(
                        context = requireContext(),
                        message = getString(R.string.msg_no_data_found)
                    )
                }
            }

            is Resource.Loading<*> -> {
                it.isLoadingShow.let { show ->
                    if (show as Boolean) {
                        showProgressDialog()
                    } else {
                        hideProgressDialog()
                    }
                }
            }

            is Resource.NoInternetError<*> -> {
                showToast(context = requireContext(), message = it.message.nullSafe())
            }
        }
    }

    private val airQualityDetailsObserver = Observer<AirQualityData> {
        setDataFromPreviousFragment(it)
    }

    override fun initViews(view: View) {
        if (!requireContext().checkInternetConnected()) {
            showToast(
                requireContext(),
                getString(R.string.msg_no_internet_home_screen)
            )
        }

        setChart()
        setGraphData()

        timer.schedule(object : TimerTask() {
            override fun run() {
                isTimerFinish = true
            }
        }, 0, TIME_INTERVAL)
    }

    override fun listeners() {
        binding.btnGoBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private var count: Float = 1f

    private fun setDataFromPreviousFragment(airQualityData: AirQualityData) {
        if (airQualityData.city != airQualityDataFromPreviousFragment.city) {
            count = 1f
            graphValues.clear()
        }

        airQualityDataFromPreviousFragment = airQualityData

        binding.tvCityName.setCityName(airQualityData.city.nullSafe())
        binding.tvCurrentAQI.setAQI(airQualityData.aqi.nullSafe().getFormattedDecimalNumber())
        binding.tvQuality.setQuality(airQualityData.aqi.nullSafe())

        graphValues.add(Entry(count, airQualityData.aqi.nullSafe().toFloat()))
        count++
        setGraphData()
    }

    private fun setAQIDataInDetails(response: String) {
        val airQualityDataItem =
            response.getAirQualityList().find { it.city == airQualityDataFromPreviousFragment.city }

        if (airQualityDataItem != null) {
            setDataFromPreviousFragment(airQualityDataItem)
        }
    }

    override fun addObservers() {
        viewModel.airQualityDetailsStatus.observe(requireActivity(), airQualityDetailsObserver)
        viewModel.fetchAirQualityListStatus.observe(requireActivity(), fetchAirQualityListObserver)
    }

    override fun removeObservers() {
        viewModel.airQualityDetailsStatus.removeObserver(airQualityDetailsObserver)
        viewModel.fetchAirQualityListStatus.removeObserver(fetchAirQualityListObserver)
    }

    private fun setChart() {
        binding.chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        binding.chart.setBackgroundColor(Color.rgb(104, 241, 175))
        binding.chart.description.isEnabled = false

        binding.chart.setTouchEnabled(true)

        binding.chart.setDrawGridBackground(false)
        binding.chart.maxHighlightDistance = 300f

        val x: XAxis = binding.chart.xAxis
        x.isEnabled = false

        val y: YAxis = binding.chart.axisLeft
        y.setLabelCount(6, false)
        y.textColor = Color.WHITE
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE

        binding.chart.axisRight.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.animateXY(2000, 2000)
    }

    private fun setGraphData() {
        binding.chart.invalidate()

        val set1: LineDataSet

        if (binding.chart.data != null &&
            binding.chart.data.dataSetCount > 0
        ) {
            set1 = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = graphValues
            binding.chart.data.notifyDataChanged()
            binding.chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(graphValues, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 1.8f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.WHITE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = Color.WHITE
            set1.fillColor = Color.WHITE
            set1.fillAlpha = 100
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter =
                IFillFormatter { _, _ ->
                    binding.chart.axisLeft.axisMinimum
                }

            // create a data object with the data sets
            val data = LineData(set1)
            data.setValueTextSize(9f)
            data.setDrawValues(false)

            // set data
            binding.chart.data = data
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AirQualityDetailsFragment().apply {
            arguments = Bundle().apply { //No bundle here to use
            }
        }
    }
}