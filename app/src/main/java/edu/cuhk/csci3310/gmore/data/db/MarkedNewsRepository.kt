package edu.cuhk.csci3310.gmore.data.db

import kotlinx.coroutines.flow.Flow

interface MarkedNewsRepository {
    suspend fun saveNews(news: MarkedNews)

    suspend fun deleteNews(news: MarkedNews)

    suspend fun getNewsById(id: String): MarkedNews?

    fun getAllMarkedNews(): Flow<List<MarkedNews>>}