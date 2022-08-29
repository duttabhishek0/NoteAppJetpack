package com.abhishek.noteappjetpack.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abhishek.noteappjetpack.utils.NEW_NOTE_ID
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var text: String
) : Parcelable {
    constructor() : this(NEW_NOTE_ID, Date(), "")
    constructor(date: Date, text: String) : this(NEW_NOTE_ID, date, text)
}