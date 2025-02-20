/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.collection.LruCache
import androidx.compose.ui.unit.dp
import com.pspdfkit.document.PdfDocumentLoader
import com.pspdfkit.example.utils.calculateBitmapSize
import com.pspdfkit.utils.Size
import java.io.File
/** Repository to hold all the tasks related to bitmap processing */
class BitmapRepository(private val context: Context) {

    /** [previewImageCache] acts as a local cache for bitmap */
    private val previewImageCache: LruCache<String, Bitmap> =
        object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return value.byteCount / 1024
            }
        }

    /** Used to retrieve bitmap from the help of [PdfDocumentLoader] */
    fun getBitmap(file: File): Bitmap {
        val id = file.path
        return previewImageCache[id] ?: try {
            val doc = PdfDocumentLoader.openDocument(context, Uri.fromFile(file))
            val size = doc.calculateBitmapSize(Size(200.dp.value, 350.dp.value))
            previewImageCache.put(id, doc.renderPageToBitmap(context, 0, size.width.toInt(), size.height.toInt()))
            previewImageCache[id] ?: throw java.lang.Exception("unable to generate bitmap with PdfDocumentLoader")
        } catch (e: Exception) {
            e.printStackTrace()
            Bitmap.createBitmap(1.dp.value.toInt(), 150.dp.value.toInt(), Bitmap.Config.ARGB_8888)
        }
    }
}
