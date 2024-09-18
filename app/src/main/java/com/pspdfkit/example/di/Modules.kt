/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.di

import com.pspdfkit.example.data.repository.BitmapRepository
import com.pspdfkit.example.data.repository.MainRepository
import com.pspdfkit.example.data.repository.PdfRepository
import com.pspdfkit.example.data.repository.SettingsRepository
import com.pspdfkit.example.ui.screens.main.MainScreenViewModel
import com.pspdfkit.example.ui.screens.pdf.PdfScreenViewModel
import com.pspdfkit.example.ui.screens.settings.SettingsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** viewModel module for DI */
var viewModel = module {
    viewModel { MainScreenViewModel(get(), get()) }
    viewModel { PdfScreenViewModel(get()) }
    viewModel { SettingsScreenViewModel(get(), get()) }
}

/** database module for DI */
var database = module {
    single { getDb(get()) }
    single { getHistoryTableDao(get()) }
}

/** repository module for DI */
var repository = module {
    single { BitmapRepository(get()) }
    single { MainRepository(get(), get()) }
    single { PdfRepository(get()) }
    single { SettingsRepository(get(), get()) }
}
