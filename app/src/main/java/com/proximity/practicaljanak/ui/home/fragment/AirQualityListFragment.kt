package com.proximity.practicaljanak.ui.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.proximity.practicaljanak.R
import com.proximity.practicaljanak.base.BaseFragment
import com.proximity.practicaljanak.common.Resource
import com.proximity.practicaljanak.databinding.FragmentAirQualityListBinding
import com.proximity.practicaljanak.extensions.*
import com.proximity.practicaljanak.interfaces.OnRecyclerViewItemClicked
import com.proximity.practicaljanak.models.AirQualityData
import com.proximity.practicaljanak.ui.home.adapter.AirQualityListAdapter
import com.proximity.practicaljanak.viewmodels.HomeScreenViewModel
import kotlin.reflect.KClass

class AirQualityListFragment : BaseFragment<HomeScreenViewModel, FragmentAirQualityListBinding>() {

    override val modelClass: KClass<HomeScreenViewModel>
        get() = HomeScreenViewModel::class

    override fun getViewBinding(): FragmentAirQualityListBinding = FragmentAirQualityListBinding.inflate(layoutInflater)

    override fun isActivityScopeViewModel(): Boolean = true

    private lateinit var airQualityListAdapter : AirQualityListAdapter
    private lateinit var airQualityDetailsFragment : AirQualityDetailsFragment
    private var airQualityDataList = ArrayList<AirQualityData>()


    private val fetchAirQualityListObserver = Observer<Any> {
        when (it) {
            is Resource.Error<*> -> {
                binding.tvNoInternet.hideView()
                showToast(context = requireContext(),message = it.message.nullSafe())
            }

            is Resource.Success<*> -> {
                hideProgressDialog()
                val response = it.data as String
                if (response.isNotEmpty()){
                    binding.rvAQIList.showView()
                    binding.tvNoInternet.hideView()

                    setAQIData(response)
                }else{
                    showToast(context = requireContext(),message = getString(R.string.msg_no_data_found))
                }
            }

            is Resource.Loading<*> -> {
                binding.tvNoInternet.hideView()
                it.isLoadingShow.let {
                    if (it as Boolean) {
                        showProgressDialog()
                    } else {
                        hideProgressDialog()
                    }
                }
            }

            is Resource.NoInternetError<*> -> {
                binding.rvAQIList.hideView()
                binding.tvNoInternet.showView()
                showToast(context = requireContext(),message = it.message.nullSafe())
            }
        }
    }

    override fun initViews(view: View) {
        setAdapter()
    }

    override fun initSetup() {
        airQualityDetailsFragment = AirQualityDetailsFragment.newInstance()
        viewModel.fetchAirQualityEntries()
    }


    private fun setAdapter() {
        airQualityListAdapter = AirQualityListAdapter(airQualityDataList, OnAQIListItemClicked())
        binding.rvAQIList.adapter = airQualityListAdapter
    }

    override fun listeners() {
        //No listener to be handle here
    }

    inner class OnAQIListItemClicked : OnRecyclerViewItemClicked{
        override fun onItemViewClicked(position: Int) {
            addOrReplaceFragment()
            viewModel.setAQIDetailsData(airQualityDataList[position])
        }
    }

    override fun addObservers() {
        viewModel.fetchAirQualityListStatus.observe(requireActivity(), fetchAirQualityListObserver)
    }

    override fun removeObservers() {
        viewModel.fetchAirQualityListStatus.removeObserver(fetchAirQualityListObserver)
    }

    private fun addOrReplaceFragment(){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        if (airQualityDetailsFragment.isAdded){
            fragmentTransaction?.show(airQualityDetailsFragment)
        }else{
            fragmentTransaction?.add(R.id.flContainer, airQualityDetailsFragment)
        }

        fragmentTransaction?.addToBackStack(airQualityDetailsFragment.getClassName())
        fragmentTransaction?.commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAQIData(response : String){
        response.getAirQualityList().forEach { data ->
            val airQualityDataListItem = airQualityDataList.find { it.city == data.city }
            if (airQualityDataListItem != null){
                airQualityDataListItem.aqi = data.aqi
            }else{
                airQualityDataList.add(data)
            }
        }

        airQualityListAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AirQualityListFragment().apply {
            arguments = Bundle().apply {
                //No bundle here to use
            }
        }
    }
}