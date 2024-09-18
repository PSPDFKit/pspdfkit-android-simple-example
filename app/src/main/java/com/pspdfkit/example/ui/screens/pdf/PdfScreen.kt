/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.pdf

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.pspdfkit.compose.theme.MainToolbarColors
import com.pspdfkit.compose.theme.ToolbarPopupColors
import com.pspdfkit.compose.theme.getUiColors
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.activity.UserInterfaceViewMode
import com.pspdfkit.configuration.theming.ThemeMode
import com.pspdfkit.document.providers.ContentResolverDataProvider
import com.pspdfkit.document.providers.DataProvider
import com.pspdfkit.example.R
import com.pspdfkit.example.ui.theme.DarkColorScheme
import com.pspdfkit.example.ui.theme.LightColorScheme
import com.pspdfkit.example.utils.Theme.isSystemSpecific
import com.pspdfkit.example.utils.getDocumentViewTheme
import com.pspdfkit.example.utils.isDarkThemeOn
import com.pspdfkit.jetpack.compose.components.MainToolbar
import com.pspdfkit.jetpack.compose.interactors.DefaultListeners
import com.pspdfkit.jetpack.compose.interactors.getDefaultDocumentManager
import com.pspdfkit.jetpack.compose.interactors.rememberDocumentState
import com.pspdfkit.jetpack.compose.utilities.ExperimentalPSPDFKitApi
import com.pspdfkit.jetpack.compose.views.DocumentView
import org.koin.androidx.compose.getViewModel
import java.io.File

/** [PdfScreen] acts as a pdf container which displays PDF. */
@Composable
fun PdfScreen(id: String, navigateTo: () -> Unit) {
    val context = LocalContext.current
    val pdfVM = getViewModel<PdfScreenViewModel>()
    val doc by pdfVM.getDocument(id).collectAsState(initial = emptyList())
    val theme by context.isDarkThemeOn().collectAsState(initial = isSystemSpecific)
    doc.firstOrNull()?.let { pdf ->
        PdfUI(
            pdf = File(pdf.path),
            context = context,
            theme = theme,
            isDark = isSystemInDarkTheme(),
            navigateTo = navigateTo
        )
    }
}

@OptIn(ExperimentalPSPDFKitApi::class)
@Composable
fun PdfUI(pdf: File, context: Context, theme: Int, isDark: Boolean, navigateTo: () -> Unit) {
    val updatedTheme = getDocumentViewTheme(theme, isDark)
    val uiColors = if (isDark) darkCustomUiColors() else lightCustomUiColors()
    var toolbarVisibility by remember { mutableStateOf(true) }
    val localDensity = LocalDensity.current
    val pdfActivityConfiguration = PdfActivityConfiguration
        .Builder(context)
        .disableDefaultToolbar()
        .setUserInterfaceViewMode(UserInterfaceViewMode.USER_INTERFACE_VIEW_MODE_AUTOMATIC)
        .themeMode(ThemeMode.DEFAULT)
        .themeDark(R.style.PSPDFCompose_Theme_Dark)
        .theme(updatedTheme)
        .build()
    val dataProvider: DataProvider by remember { mutableStateOf(ContentResolverDataProvider(pdf.toUri())) }
    val documentState = rememberDocumentState(dataProvider, pdfActivityConfiguration)

    // This is a workaround for the compose toolbar obstructing some views such as the thumbnail grid.
    var toolbarHeight by remember { mutableStateOf(0.dp) }
    val enableViewSpacer by documentState.viewWithOverlappingToolbarShown.collectAsState()
    val viewSpacerHeight by remember { derivedStateOf { if (enableViewSpacer && toolbarVisibility) toolbarHeight else 0.dp } }

    Scaffold {
        Box(modifier = Modifier.padding(it), contentAlignment = Alignment.TopCenter) {
            val density = LocalDensity.current

            Column {
                Box(modifier = Modifier.height(viewSpacerHeight))
                DocumentView(
                    documentState,
                    modifier = Modifier.fillMaxSize(),
                    // Examples showing how to hook into the callbacks.
                    documentManager = getDefaultDocumentManager(
                        documentListener = DefaultListeners.documentListeners(onDocumentLoaded = {
                            Toast.makeText(context, "document Loaded", Toast.LENGTH_LONG).show()
                        }),
                        annotationListener = DefaultListeners.annotationListeners(
                            onAnnotationSelected = { annotation, _ ->
                                Toast.makeText(context, "${annotation.type} selected", Toast.LENGTH_LONG).show()
                            },
                            onAnnotationDeselected = { annotation, _ ->
                                Toast.makeText(context, "${annotation.type} deselected", Toast.LENGTH_LONG).show()
                            }
                        ),
                        uiListener = DefaultListeners.uiListeners(onImmersiveModeEnabled = { visibility ->
                            toolbarVisibility = visibility
                        })
                    )
                )
            }

            AnimatedVisibility(
                visible = toolbarVisibility,
                enter = slideInVertically { with(density) { -40.dp.roundToPx() } } +
                    expandVertically(expandFrom = Alignment.Top) +
                    fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                MainToolbar(
                    documentState = documentState,
                    colorScheme = uiColors,
                    navigationIcon = {
                        IconButton(onClick = { navigateTo.invoke() }) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = it)
                        }
                    },
                    onHeightChanged = { height ->
                        toolbarHeight = with(localDensity) { height.toDp() }
                    }
                )
            }
        }
    }
}

// Here we can customise the specific views for example the main toolbar.
@Composable
fun lightCustomUiColors() = getUiColors().copy(
    mainToolbar = MainToolbarColors(
        backgroundColor = LightColorScheme.primary,
        textColor = Color.Black,
        popup = ToolbarPopupColors(backgroundColor = LightColorScheme.primary),
        titleTextColor = Color.Black
    )
)

@Composable
fun darkCustomUiColors() = getUiColors().copy(
    mainToolbar = MainToolbarColors(
        backgroundColor = DarkColorScheme.primary,
        textColor = Color.White,
        popup = ToolbarPopupColors(backgroundColor = DarkColorScheme.primary),
        titleTextColor = Color.White
    )
)
