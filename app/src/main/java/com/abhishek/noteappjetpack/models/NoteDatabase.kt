package com.abhishek.noteappjetpack.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abhishek.noteappjetpack.`interface`.NoteDao
import com.abhishek.noteappjetpack.utils.DateConverter

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao?

    companion object {
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "notes.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}