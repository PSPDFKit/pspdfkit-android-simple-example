/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.utils

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pspdfkit.example.R
import com.pspdfkit.example.utils.Theme.DynamicColors
import com.pspdfkit.example.utils.Theme.isDark
import com.pspdfkit.example.utils.Theme.isLight
import com.pspdfkit.example.utils.Theme.isSystemSpecific
import com.pspdfkit.jetpack.compose.views.DocumentView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * extensions to store dark mode and dynamic mode as int and fetch as state
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val isDarkKey = intPreferencesKey("isDarkKey")
val isDynamicKey = intPreferencesKey("isDynamicKey")

fun Context.isDarkThemeOn() = dataStore.data
    .map { preferences ->
        // No type safety.
        preferences[isDarkKey] ?: isSystemSpecific
    }

fun Context.isThemeDynamic() = dataStore.data
    .map { preferences ->
        // No type safety.
        preferences[isDynamicKey] ?: DynamicColors.enabled
    }

@Composable
fun isSystemInDarkThemeCustom(): Boolean {
    val context = LocalContext.current
    val prefs = runBlocking { context.dataStore.data.first() }
    return when (
        context.isDarkThemeOn()
            .collectAsState(initial = prefs[isDarkKey] ?: isSystemSpecific).value
    ) {
        isDark -> true
        isLight -> false
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun isThemeDynamic(): Boolean {
    val context = LocalContext.current
    val prefs = runBlocking { context.dataStore.data.first() }
    return when (
        context.isThemeDynamic()
            .collectAsState(initial = prefs[isDynamicKey] ?: DynamicColors.enabled).value
    ) {
        DynamicColors.enabled -> true
        else -> false
    }
}

/** Theme constants to manage night mode and Dynamic color feature */
object Theme {
    const val isDark = 2
    const val isLight = 1
    const val isSystemSpecific = 0
    object DynamicColors {
        const val enabled = 0
        const val disabled = 1
    }
}

/**
 * [getDocumentViewTheme] provides custom theme for [DocumentView]
 * @param theme provide int value which are stored in app level data store.
 * @param isDark provides if Dark mode is enabled at OS level.
 *
 * In [getDocumentViewTheme] if theme is [isSystemSpecific] we check the system level dark mode
 * else provide theme according to the local preference.
 *
 * */
fun getDocumentViewTheme(theme: Int, isDark: Boolean) = if (theme == isSystemSpecific) getTheme(isDark) else getTheme(theme == Theme.isDark)

private fun getTheme(isDark: Boolean) = if (isDark) { R.style.PSPDFCompose_Theme_Dark } else { R.style.PSPDFCompose_Theme_Light }
