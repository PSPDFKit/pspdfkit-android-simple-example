/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.utils

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map

/**
 *  MainScreen Grid and list ui configuration.
 */

val isListType = intPreferencesKey("isListKey")

fun Context.isList() = dataStore.data
    .map { preferences ->
        // No type safety.
        preferences[isListType] ?: 1
    }
