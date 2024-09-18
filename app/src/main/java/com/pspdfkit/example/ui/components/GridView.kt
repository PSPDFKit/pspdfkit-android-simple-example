/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

/** Composable for Grid layout */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridView(
    pdfList: List<HistoryTable>,
    navigateTo: (String) -> Unit,
    bitmap: suspend(HistoryTable) -> ImageBitmap,
    filePicker: () -> Unit
) {
    val localPdf = pdfList.filter { it.type == HistoryType.LOCAL.type() }
    val recentPdf = pdfList.filter { it.type != HistoryType.LOCAL.type() }
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        val span: (LazyGridItemSpanScope.() -> GridItemSpan) = {
            GridItemSpan(3)
        }

        item(span = span) {
            HeadingText(text = "External Directory")
        }
        item {
            ElevatedCard(
                onClick = { filePicker.invoke() },
                modifier = Modifier.padding(6.dp, 6.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.external_file_sources),
                        contentDescription = "External Files",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .padding(24.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
                Column(
                    modifier = Modifier.padding(12.dp, 16.dp)
                ) {
                    Text(
                        text = "External Files",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        if (localPdf.isNotEmpty()) {
            item(span = span) {
                HeadingText(text = "Local Files")
            }

            items(localPdf) {
                GridItem(it, onClick = navigateTo, bitmap = bitmap)
            }
        }
        if (recentPdf.isNotEmpty()) {
            item(span = span) {
                HeadingText(text = "Recently Opened")
            }
            items(recentPdf) {
                GridItem(it, onClick = navigateTo, bitmap = bitmap)
            }
        }
    }
}

@Preview
@Composable
fun GridViewPreview() {
    val context = LocalContext.current
    val path = context.getFile(DEMO_DOCUMENT_ASSET_NAME).path
    val list = ArrayList<HistoryTable>().apply {
        repeat(2) {
            add(local(path))
        }
        repeat(5) {
            add(recent(path))
        }
    }.toList()

    GridView(list, { _ -> }, { Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap() }, {})
}
