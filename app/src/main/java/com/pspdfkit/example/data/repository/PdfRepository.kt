/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.data.repository

import com.pspdfkit.example.data.local.HistoryDao

/** [PdfRepository] holds functionality related to pdf. */
class PdfRepository(private val historyDao: HistoryDao) {
    /** retrieves pdf entry from db by primary key */
    fun getPdf(id: String) = historyDao.getHistoryById(id)
}
