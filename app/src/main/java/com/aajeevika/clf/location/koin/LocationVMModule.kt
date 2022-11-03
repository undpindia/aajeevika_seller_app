package com.aajeevika.clf.location.koin

import com.aajeevika.clf.location.viewModel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationViewModelModule = module {
    viewModel { LocationViewModel() }
}