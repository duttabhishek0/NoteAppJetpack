package com.abhishek.noteappjetpack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.noteappjetpack.models.NoteDatabase
import com.abhishek.noteappjetpack.models.NoteEntity
import com.abhishek.noteappjetpack.utils.DataProviderUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel (app: Application) : AndroidViewModel(app) {

    private val database = NoteDatabase.getInstance(app)
    val notesList = database?.noteDao()?.getAll()

    fun addSampleData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sampleNotes = DataProviderUseCase.getNotes()
                database?.noteDao()?.insertAll(sampleNotes)
            }
        }
    }

    fun deleteNotes(selectedNotes: List<NoteEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.noteDao()?.deleteNotes(selectedNotes)
            }
        }

    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.noteDao()?.deleteAll()
            }
        }
    }

}