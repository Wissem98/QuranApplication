package com.ems.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ems.myapplication.models.Verse
import com.ems.myapplication.models.Word

@Database(entities = [Word::class, Verse::class], version = 13)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun verseDao(): VerseDao

    companion object {
        @Volatile
        private var INSTANCE: QuranDatabase? = null

        fun getDatabase(context: Context): QuranDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuranDatabase::class.java,
                    "word_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}

