package edu.cuhk.csci3310.gmore.data.db

import kotlinx.coroutines.flow.Flow

class MarkedNewsRepositoryImpl(private val dao: MarkedNewsDao): MarkedNewsRepository {
    override suspend fun saveNews(news: MarkedNews) {
        dao.saveNews(news)
    }

    override suspend fun deleteNews(news: MarkedNews) {
        dao.deleteNews(news)
    }

    override suspend fun getNewsById(id: String): MarkedNews? {
        return dao.getNewsById(id)
    }

    override fun getAllMarkedNews(): Flow<List<MarkedNews>> {
        return dao.getAllMarkedNews()
    }
}