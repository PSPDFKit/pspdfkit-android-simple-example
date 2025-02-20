/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pspdfkit.example.R
import com.pspdfkit.example.models.HistoryTable
import com.pspdfkit.example.models.HistoryType
import com.pspdfkit.example.models.local
import com.pspdfkit.example.models.recent
import com.pspdfkit.example.models.type
import com.pspdfkit.example.utils.DEMO_DOCUMENT_ASSET_NAME
import com.pspdfkit.example.utils.getFile

/** Composable for ListView layout */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
    pdfList: List<HistoryTable>,
    navigateTo: (String) -> Unit,
    bitmap: suspend (HistoryTable) -> ImageBitmap,
    filePicker: () -> Unit
) {
    val localPdf = pdfList.filter { it.type == HistoryType.LOCAL.type() }
    val recentPdf = pdfList.filter { it.type != HistoryType.LOCAL.type() }
    LazyColumn {
        item {
            HeadingText(text = "External Directory")
        }
        item {
            ListItem(
                modifier = Modifier.clickable { filePicker.invoke() },
                headlineContent = {
                    Text(
                        "External Files",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text("some text explaining external storage")
                },
                leadingContent = {
                    Image(
                        painter = painterResource(id = R.drawable.external_file_sources),
                        contentDescription = "External files",
                        modifier =
                        Modifier
                            .width(50.dp)
                            .height(38.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }
        if (localPdf.isNotEmpty()) {
            item {
                HeadingText(text = "Local Files")
            }

            items(localPdf) {
                ListItem(it, onClick = navigateTo, bitmap = bitmap)
            }
        }
        if (recentPdf.isNotEmpty()) {
            item {
                HeadingText(text = "Recently Opened")
            }
            items(recentPdf) {
                ListItem(it, onClick = navigateTo, bitmap = bitmap)
            }
        }
    }
}

@Preview
@Composable
fun ListViewPreview() {
    val context = LocalContext.current
    val path = context.getFile(DEMO_DOCUMENT_ASSET_NAME).path
    val list =
        ArrayList<HistoryTable>()
            .apply {
                repeat(2) {
                    add(local(path))
                }
                repeat(5) {
                    add(recent(path))
                }
            }.toList()

    ListView(
        list,
        { _ -> },
        { Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap() },
        {}
    )
}
