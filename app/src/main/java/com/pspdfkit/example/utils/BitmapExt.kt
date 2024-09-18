/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.utils

import com.pspdfkit.document.PdfDocument
import com.pspdfkit.utils.Size
/**
 * bitmap size conversion
 */
fun PdfDocument.calculateBitmapSize(availableSpace: Size): Size {
    val pageSize = getPageSize(0)
    val ratio: Float = if (pageSize.width > pageSize.height) {
        availableSpace.width / pageSize.width
    } else {
        availableSpace.height / pageSize.height
    }
    return Size(pageSize.width * ratio, pageSize.height * ratio)
}
