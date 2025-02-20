/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.pspdfkit.example.models.HistoryTable
import kotlinx.coroutines.flow.Flow

/** Database class required to hold pdf access history */
@Database(version = 1, entities = [HistoryTable::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}

/** Dao class for [HistoryTable]*/
@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyTable: HistoryTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(historyList: List<HistoryTable>)

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): Flow<List<HistoryTable>>

    @Query("SELECT * FROM history_table where id = :id")
    fun getHistoryById(id: String): Flow<List<HistoryTable>>

    @Query("DELETE FROM history_table")
    suspend fun deleteAll()
}
