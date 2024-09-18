/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

/** Files retrieval and copy functionality */

const val DEMO_DOCUMENT_ASSET_NAME = "demo.pdf"

fun Context.getWorkingDir() = File(cacheDir, "pdf")
fun Context.getFile(name: String) = File(getWorkingDir(), name)

fun Context.doesLocalFileExists() = getFile(DEMO_DOCUMENT_ASSET_NAME).exists()
suspend fun File.copyFile(context: Context, finish: suspend () -> Unit) {
    val assetManager = context.assets
    runCatching {
        val out: OutputStream = FileOutputStream(this)
        val buffer = ByteArray(1024)
        assetManager.open(DEMO_DOCUMENT_ASSET_NAME).let {
            var read = it.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
                read = it.read(buffer)
            }
        }
    }.onSuccess {
        finish.invoke()
    }
}

suspend fun copyExternalFile(context: Context, uri: Uri, finish: suspend (File) -> Unit) {
    val name = getName(uri, context) ?: "${UUID.randomUUID()}.pdf"
    val file = File(File(context.cacheDir, "pdf"), name)
    val inputStream = context.contentResolver.openInputStream(uri)
    runCatching {
        val out: OutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        inputStream?.let {
            var read = it.read(buffer)
            while (read != -1) {
                out.write(buffer, 0, read)
                read = it.read(buffer)
            }
        }
    }.onSuccess {
        inputStream?.close()
        finish.invoke(file)
    }
}

fun getName(uri: Uri, context: Context): String? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    }
}
