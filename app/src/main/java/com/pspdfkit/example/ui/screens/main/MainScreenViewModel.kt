/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pspdfkit.example.data.repository.BitmapRepository
import com.pspdfkit.example.data.repository.MainRepository
import com.pspdfkit.example.models.HistoryTable
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class MainScreenViewModel(private val repository: MainRepository, private val bitmapRepository: BitmapRepository) : ViewModel() {

    val copyAssets = repository.assetFileTransferFlow.asStateFlow()
    fun init() {
        viewModelScope.launch {
            repository.init()
        }
    }

    fun getBitmap(file: File) = bitmapRepository.getBitmap(file)
    fun addRecentPdf(historyTable: HistoryTable) {
        viewModelScope.launch { repository.addRecentPdf(historyTable) }
    }

    val pdfList = repository.getAllPdf()
}
