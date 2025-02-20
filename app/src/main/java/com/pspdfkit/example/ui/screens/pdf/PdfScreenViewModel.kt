/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.pdf

import androidx.lifecycle.ViewModel
import com.pspdfkit.example.data.repository.PdfRepository

class PdfScreenViewModel(private val pdfRepository: PdfRepository) : ViewModel() {
    fun getDocument(id: String) = pdfRepository.getPdf(id)
}
