/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pspdfkit.example.ui.screens.Keys
import com.pspdfkit.example.ui.screens.Screens
import com.pspdfkit.example.ui.screens.main.MainScreen
import com.pspdfkit.example.ui.screens.pdf.PdfScreen
import com.pspdfkit.example.ui.screens.settings.SettingsScreen

/** [Navigation] composable holds all the navigation logic and serves as a list of index for all screens. */
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.MAIN_SCREEN
    ) {
        composable(Screens.MAIN_SCREEN) { MainScreen { path -> navController.navigate(path) } }
        composable(Screens.SETTINGS_SCREEN) { SettingsScreen { navController.navigateUp() } }
        composable("${Screens.PDF_SCREEN}/{${Keys.PDF_KEY}}", arguments = listOf(navArgument(Keys.PDF_KEY) { type = NavType.StringType })) {
            val pdfKey = requireNotNull(it.arguments).getString(Keys.PDF_KEY) ?: ""
            PdfScreen(pdfKey) { navController.navigateUp() }
        }
    }
}
