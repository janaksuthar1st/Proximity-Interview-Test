package com.proximity.practicaljanak.di

import com.proximity.practicaljanak.repository.network.AirQualityNetworkRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single { AirQualityNetworkRepository() }
}