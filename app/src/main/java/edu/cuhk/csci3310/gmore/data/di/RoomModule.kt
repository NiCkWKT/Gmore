package edu.cuhk.csci3310.gmore.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.cuhk.csci3310.gmore.data.db.MarkedNewsDatabase
import edu.cuhk.csci3310.gmore.data.db.MarkedNewsRepository
import edu.cuhk.csci3310.gmore.data.db.MarkedNewsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideMarkedNewsDatabase(app: Application): MarkedNewsDatabase {
        return Room.databaseBuilder(
            app,
            MarkedNewsDatabase::class.java, "marked_news_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMarkedNewsRepository(db: MarkedNewsDatabase): MarkedNewsRepository {
        return MarkedNewsRepositoryImpl(db.dao)
    }
}