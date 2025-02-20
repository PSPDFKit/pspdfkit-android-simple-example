/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens

/**
 * Screens Object - holds navigation constants
 */
object Screens {
    const val MAIN_SCREEN = "MainScreen"
    const val PDF_SCREEN = "PdfScreen"
    const val SETTINGS_SCREEN = "SettingsScreen"
}

/**
 * Keys Object - holds navigation keys
 */
object Keys {
    const val PDF_KEY = "PdfKey"
}

fun Screens.pdfScreenWithId(id: String) = "$PDF_SCREEN/$id"
