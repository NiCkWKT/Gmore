package edu.cuhk.csci3310.gmore.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MarkedNews::class],
    version = 1
)
abstract class MarkedNewsDatabase: RoomDatabase() {
    abstract val dao: MarkedNewsDao
}