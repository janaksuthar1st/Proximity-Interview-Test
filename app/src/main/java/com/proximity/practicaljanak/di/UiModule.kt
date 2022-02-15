package com.proximity.practicaljanak.di

import com.proximity.practicaljanak.viewmodels.HomeScreenViewModel
import com.proximity.practicaljanak.viewmodels.SplashViewModel
import org.koin.dsl.module

import org.koin.androidx.viewmodel.dsl.viewModel

val uiModules = arrayOf(
    module {
        viewModel { SplashViewModel(get()) }
        viewModel { HomeScreenViewModel(get(),get()) }
    }
)