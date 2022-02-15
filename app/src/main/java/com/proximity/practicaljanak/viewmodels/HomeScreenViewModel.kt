package com.proximity.practicaljanak.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.proximity.practicaljanak.R
import com.proximity.practicaljanak.common.Resource
import com.proximity.practicaljanak.models.AirQualityData
import com.proximity.practicaljanak.repository.network.AirQualityNetworkRepository
import com.proximity.practicaljanak.utils.CommonUtils.checkInternetConnected


class HomeScreenViewModel(
    application: Application,
    private val airQualityNetworkRepository: AirQualityNetworkRepository
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val mContext = application.applicationContext

    val fetchAirQualityListStatus: MediatorLiveData<Any> by lazy {
        MediatorLiveData<Any>()
    }

    val airQualityDetailsStatus: MediatorLiveData<AirQualityData> by lazy {
        MediatorLiveData<AirQualityData>()
    }

    fun fetchAirQualityEntries(){
        if (!mContext.checkInternetConnected()) {
            fetchAirQualityListStatus.value =
                Resource.NoInternetError<String>(mContext.getString(R.string.default_internet_message))
        } else {
            fetchAirQualityListStatus.addSource(
                airQualityNetworkRepository.fetchAllAirQualityData(true)
            ) {
                fetchAirQualityListStatus.value = it
            }
        }
    }

    fun setAQIDetailsData(airQualityData: AirQualityData) {
        airQualityDetailsStatus.postValue(airQualityData)
    }
}