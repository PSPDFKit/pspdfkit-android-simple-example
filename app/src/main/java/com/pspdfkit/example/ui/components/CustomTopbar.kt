/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import com.pspdfkit.example.R
import com.pspdfkit.example.utils.dataStore
import com.pspdfkit.example.utils.isListType
import kotlinx.coroutines.launch

/** Custom implementation of ToAppBar */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(drawerState: DrawerState, scrollBehavior: TopAppBarScrollBehavior, toggle: Int) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        )
        LargeTopAppBar(
            title = {
                Text(stringResource(id = R.string.app_name), maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = {
                    scope.launch {
                        context.dataStore.edit { settings ->
                            val type = settings[isListType]
                            settings[isListType] = if (type == 0) 1 else 0
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (toggle == 0) Icons.Filled.GridView else Icons.AutoMirrored.Filled.List,
                        contentDescription = "Grid_or_list"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomAppbarPreview() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    CustomTopAppBar(drawerState, scrollBehavior, 0)
}
