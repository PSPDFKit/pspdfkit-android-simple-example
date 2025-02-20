/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.main

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pspdfkit.example.R
import com.pspdfkit.example.models.HistoryTable
import com.pspdfkit.example.models.recent
import com.pspdfkit.example.ui.components.CustomTopAppBar
import com.pspdfkit.example.ui.components.GridView
import com.pspdfkit.example.ui.components.ListView
import com.pspdfkit.example.ui.screens.Screens
import com.pspdfkit.example.ui.screens.pdf.PdfScreen
import com.pspdfkit.example.ui.screens.pdfScreenWithId
import com.pspdfkit.example.utils.copyExternalFile
import com.pspdfkit.example.utils.isList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.io.File

/** [MainScreen] provides various navigation option to move around in app.
 * It holds list of Pdf entries in form of thumbnails which can be further
 * opened in [PdfScreen]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navigateTo: (String) -> Unit) {
    val mainVM = getViewModel<MainScreenViewModel>()
    val init by mainVM.copyAssets.collectAsState()
    val pdfList by mainVM.pdfList.collectAsState(emptyList())
    val context = LocalContext.current
    val toggle by context.isList().collectAsState(initial = 1)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutine = rememberCoroutineScope()

    val filePickerActivityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data
            it.data?.data?.let {
                coroutine.launch {
                    // todo can be converted to states in VM
                    copyExternalFile(context, it) {
                        val recent = recent(it.path)
                        mainVM.addRecentPdf(recent)
                        navigateTo.invoke(Screens.pdfScreenWithId(recent.id))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        mainVM.init()
    }
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Spacer(Modifier.height(55.dp))
            NavigationDrawerItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                label = { Text(stringResource(id = R.string.settings)) },
                selected = false,
                onClick = {
                    coroutine.launch { drawerState.close() }
                    navigateTo.invoke(Screens.SETTINGS_SCREEN)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }, content = {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = { CustomTopAppBar(drawerState, scrollBehavior, toggle) }
            ) {
                Box(
                    Modifier
                        .padding(it)
                        .padding(8.dp, 0.dp)
                ) {
                    if (init) {
                        MainUI(toggle == 0, pdfList, navigateTo, { item -> mainVM.getBitmap(File(item.path)).asImageBitmap() }) {
                            filePickerActivityResultLauncher.launch(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "application/*"
                                }
                            )
                        }
                    }
                }
            }
        })
}

@Composable
fun MainUI(
    isList: Boolean,
    pdfList: List<HistoryTable>,
    navigateTo: (String) -> Unit,
    bitmap: suspend(HistoryTable) -> ImageBitmap,
    filePicker: () -> Unit
) {
    if (isList) {
        ListView(pdfList = pdfList, navigateTo = navigateTo, bitmap = bitmap, filePicker = filePicker)
    } else {
        GridView(pdfList = pdfList, navigateTo = navigateTo, bitmap = bitmap, filePicker = filePicker)
    }
}
