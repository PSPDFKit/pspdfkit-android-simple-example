/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pspdfkit.example.R
import com.pspdfkit.example.models.HistoryTable
import com.pspdfkit.example.models.recent
import com.pspdfkit.example.ui.screens.Screens
import com.pspdfkit.example.ui.screens.pdfScreenWithId
import com.pspdfkit.example.utils.DEMO_DOCUMENT_ASSET_NAME
import com.pspdfkit.example.utils.getFile
import com.pspdfkit.example.utils.timeAgoInSeconds
import kotlinx.coroutines.launch
import java.io.File

/** Composable for Grid item element */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridItem(historyTable: HistoryTable, onClick: (String) -> Unit, bitmap: suspend(HistoryTable) -> ImageBitmap) {
    var localBitmap by remember {
        mutableStateOf(ImageBitmap(1, 1, ImageBitmapConfig.Argb8888))
    }
    val file = File(historyTable.path)
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(file) {
        coroutine.launch {
            localBitmap = bitmap.invoke(historyTable)
        }
    }
    ElevatedCard(
        onClick = { onClick.invoke(Screens.pdfScreenWithId(historyTable.id)) },
        modifier = Modifier.padding(6.dp, 6.dp)
    ) {
        Box {
            Image(
                bitmap = localBitmap,
                contentDescription = file.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.FillWidth
            )
        }
        Column(
            modifier = Modifier.padding(12.dp, 16.dp)
        ) {
            Text(
                text = file.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = file.lastModified().timeAgoInSeconds(),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(widthDp = 200, heightDp = 230)
@Composable
fun GridItemPreview() {
    val context = LocalContext.current
    val path = context.getFile(DEMO_DOCUMENT_ASSET_NAME).path
    GridItem(recent(path), { _ -> }) {
        ImageBitmap.imageResource(context.resources, R.drawable.external_file_sources)
    }
}
