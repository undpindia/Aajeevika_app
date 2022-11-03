package com.aajeevika.individual.location.koin

import com.aajeevika.individual.location.viewModel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationViewModelModule = module {
    viewModel { LocationViewModel() }
}