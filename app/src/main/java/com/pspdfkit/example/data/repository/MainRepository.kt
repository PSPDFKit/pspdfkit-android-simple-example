/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.data.repository

import android.content.Context
import com.pspdfkit.example.data.local.HistoryDao
import com.pspdfkit.example.models.HistoryTable
import com.pspdfkit.example.models.HistoryType.LOCAL
import com.pspdfkit.example.models.HistoryType.RECENT
import com.pspdfkit.example.models.local
import com.pspdfkit.example.utils.DEMO_DOCUMENT_ASSET_NAME
import com.pspdfkit.example.utils.copyFile
import com.pspdfkit.example.utils.doesLocalFileExists
import com.pspdfkit.example.utils.getFile
import com.pspdfkit.example.utils.getWorkingDir
import kotlinx.coroutines.flow.MutableStateFlow

/** [MainRepository] mostly manages all the functions related to Main screen  */
class MainRepository(private val historyDao: HistoryDao, private val context: Context) {
    init {
        /** initialises the specific working cache directory */
        context.getWorkingDir().let { if (!it.exists()) it.mkdir() }
    }

    /** pre provided pdf for testing */
    val file = context.getFile(DEMO_DOCUMENT_ASSET_NAME)
    val assetFileTransferFlow = MutableStateFlow(file.exists())

    /** adds [RECENT] type pdf to database */
    private suspend fun insertLocalPdf(path: String) {
        historyDao.insert(local(path))
    }

    /** retrieves all entries from database */
    fun getAllPdf() = historyDao.getAllHistory()

    /** functionality to copy local pdf file from assets to internal working directory. */
    suspend fun init() {
        if (context.doesLocalFileExists()) {
            assetFileTransferFlow.emit(true)
        } else {
            file.copyFile(context) {
                insertLocalPdf(file.path)
                assetFileTransferFlow.emit(true)
            }
        }
    }

    /** adds [LOCAL] type pdf to database */
    suspend fun addRecentPdf(historyTable: HistoryTable) {
        historyDao.insert(historyTable)
    }
}
