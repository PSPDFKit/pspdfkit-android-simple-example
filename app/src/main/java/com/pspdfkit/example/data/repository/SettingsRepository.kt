/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.pspdfkit.example.data.local.HistoryDao
import com.pspdfkit.example.utils.dataStore
import com.pspdfkit.example.utils.getWorkingDir
import com.pspdfkit.example.utils.isDarkKey
import com.pspdfkit.example.utils.isDynamicKey

/** [SettingsRepository] contains all the functionality related to settings. */
class SettingsRepository(private val context: Context, private val historyDao: HistoryDao) {

    // Clear all data from local Db and re-fetch pdf files
    suspend fun clearAllData(finish: suspend () -> Unit) {
        val dir = context.getWorkingDir()
        historyDao.deleteAll()

        if (dir.exists()) {
            val safelyDeleted = dir.deleteRecursively()
            if (safelyDeleted) {
                dir.mkdir()
                finish.invoke()
            }
        }
    }

    // Update Dynamic theme in dataStore
    suspend fun updateDynamicTheme(i: Int) {
        context.dataStore.edit { settings ->
            settings[isDynamicKey] = i
        }
    }

    // Update dark mode in dataStore
    suspend fun updateDarkTheme(i: Int) {
        context.dataStore.edit { settings ->
            settings[isDarkKey] = i
        }
    }
}
