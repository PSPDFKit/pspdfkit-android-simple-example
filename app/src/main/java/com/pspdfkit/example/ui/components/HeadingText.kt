/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** custom heading composable */
@Composable
fun HeadingText(text: String) {
    Text(text = text, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp, 12.dp))
}

@Preview(showBackground = true)
@Composable
fun HeadingTextPreview() {
    HeadingText("External Files")
}
