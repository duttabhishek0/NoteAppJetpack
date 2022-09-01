package com.abhishek.noteappjetpack

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abhishek.noteappjetpack.`interface`.NoteDao
import com.abhishek.noteappjetpack.models.NoteDatabase
import com.abhishek.noteappjetpack.models.NoteEntity
import com.abhishek.noteappjetpack.utils.DataProviderUseCase
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseEntityUnitTest {
    private lateinit var notesDao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider
            .getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, NoteDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndRead() {
        val note = NoteEntity(
            DataProviderUseCase.getDate(0),
            "This is a test note"
        )
        notesDao.insertNote(note)
        val byId = notesDao.getNoteById(1)
        assertThat(byId, equalTo(1))
    }
}