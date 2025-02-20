/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.screens.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pspdfkit.example.R
import com.pspdfkit.example.utils.Theme
import com.pspdfkit.example.utils.Theme.isDark
import com.pspdfkit.example.utils.Theme.isLight
import com.pspdfkit.example.utils.Theme.isSystemSpecific
import com.pspdfkit.example.utils.isDarkThemeOn
import com.pspdfkit.example.utils.isThemeDynamic
import org.koin.androidx.compose.getViewModel

/** Displays provided settings in App. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigateTo: () -> Unit) {
    val viewModel = getViewModel<SettingsScreenViewModel>()
    // Default is false (not showing)
    var alertDialogVisibility by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Settings") }, navigationIcon = {
            IconButton(onClick = { navigateTo.invoke() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) {
        val context = LocalContext.current
        val theme = context.isDarkThemeOn().collectAsState(initial = 0)
        val dynamic = context.isThemeDynamic().collectAsState(initial = 0)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .width(600.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp, 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Column(modifier = Modifier.weight(1f, true)) {
                        Text(
                            "Dynamic Colors",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            stringResource(
                                id = when (dynamic.value) {
                                    Theme.DynamicColors.enabled -> R.string.dynamic_mode_selected
                                    else -> R.string.dynamic_mode_unselected
                                }
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                    Switch(
                        colors = SwitchDefaults.colors(uncheckedThumbColor = MaterialTheme.colorScheme.secondary),
                        onCheckedChange = {
                            viewModel.updateDynamicTheme(if (it) Theme.DynamicColors.enabled else Theme.DynamicColors.disabled)
                        },
                        checked = dynamic.value == Theme.DynamicColors.enabled
                    )
                }

                Row(
                    modifier = Modifier.padding(16.dp, 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Column(modifier = Modifier.weight(1f, true)) {
                        Text(
                            "Theme",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            stringResource(
                                id = when (theme.value) {
                                    isSystemSpecific -> R.string.default_theme
                                    else -> R.string.custom_theme
                                }
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                    Switch(
                        colors = SwitchDefaults.colors(uncheckedThumbColor = MaterialTheme.colorScheme.secondary),
                        onCheckedChange = {
                            viewModel.updateDarkTheme(if (it) isLight else isSystemSpecific)
                        },
                        checked = theme.value != isSystemSpecific
                    )
                }
                if (theme.value != isSystemSpecific) {
                    Row(
                        modifier = Modifier.padding(16.dp, 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Column(modifier = Modifier.weight(1f, true)) {
                            Text(
                                "Dark Mode",
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                stringResource(
                                    id = when (theme.value) {
                                        1 -> R.string.light_mode_selected
                                        2 -> R.string.dark_mode_selected
                                        else -> R.string.default_theme
                                    }
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Start
                            )
                        }
                        Switch(
                            colors = SwitchDefaults.colors(uncheckedThumbColor = MaterialTheme.colorScheme.secondary),
                            onCheckedChange = {
                                viewModel.updateDarkTheme(if (it) isDark else isLight)
                            },
                            checked = theme.value == isDark
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clickable {
                            alertDialogVisibility = true
                        }
                        .padding(16.dp, 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f, true)) {
                        Text(
                            stringResource(
                                id = R.string.cache_title
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            stringResource(
                                id = R.string.cache_summary
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                AnimatedVisibility(visible = alertDialogVisibility) {
                    AlertDialog(onDismissRequest = { alertDialogVisibility = false }, title = {
                        Text(text = stringResource(id = R.string.clear_cache))
                    }, text = {
                            Text(text = stringResource(id = R.string.cache_summary))
                        }, confirmButton = {
                            TextButton(onClick = {
                                alertDialogVisibility = false
                                viewModel.clearAllData()
                                Toast.makeText(context, R.string.storage_cleared, Toast.LENGTH_SHORT)
                                    .show()
                            }) {
                                Text("Ok")
                            }
                        }, dismissButton = {
                            TextButton(onClick = {
                                alertDialogVisibility = false
                            }) {
                                Text("Cancel")
                            }
                        })
                }
            }
        }
    }
}
