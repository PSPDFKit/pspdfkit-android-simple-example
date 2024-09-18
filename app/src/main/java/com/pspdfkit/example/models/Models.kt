/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/** [HistoryTable] is a database table which acts as container to hold necessary data required.  */
@Entity(tableName = "history_table", indices = [Index(value = ["id", "path"], unique = true)])
data class HistoryTable(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val path: String,
    val type: Int
)

/** creates [HistoryTable] instance with type [HistoryType.RECENT] */
fun recent(path: String) = HistoryTable(path = path, type = HistoryType.RECENT.type())

/** creates [HistoryTable] instance with type [HistoryType.LOCAL] */
fun local(path: String) = HistoryTable(path = path, type = HistoryType.LOCAL.type())

/** Enum to differentiate pdf entries type*/
enum class HistoryType {
    LOCAL, RECENT
}

/** [HistoryType] extension to convert enum to int values. */
fun HistoryType.type() = when (this) {
    HistoryType.LOCAL -> 0
    else -> 1
}
