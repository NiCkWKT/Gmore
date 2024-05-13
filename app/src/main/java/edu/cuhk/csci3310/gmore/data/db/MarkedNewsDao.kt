package edu.cuhk.csci3310.gmore.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(news: MarkedNews)

    @Delete
    suspend fun deleteNews(news: MarkedNews)

    @Query("SELECT * FROM markednews WHERE id = :id")
    suspend fun getNewsById(id: Int): MarkedNews?

    @Query("SELECT * FROM markednews")
    fun getAllMarkedNews(): Flow<List<MarkedNews>>
}