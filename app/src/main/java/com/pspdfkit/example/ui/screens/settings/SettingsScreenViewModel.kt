/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pspdfkit.example.data.repository.MainRepository
import com.pspdfkit.example.data.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val settingsRepository: SettingsRepository, private val mainRepository: MainRepository) : ViewModel() {
    fun updateDynamicTheme(i: Int) {
        viewModelScope.launch {
            settingsRepository.updateDynamicTheme(i)
        }
    }

    fun updateDarkTheme(i: Int) {
        viewModelScope.launch {
            settingsRepository.updateDarkTheme(i)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            settingsRepository.clearAllData {
                mainRepository.init()
            }
        }
    }
}
