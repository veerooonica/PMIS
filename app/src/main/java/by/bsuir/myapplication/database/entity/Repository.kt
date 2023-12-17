package by.bsuir.myapplication.database.entity

import by.bsuir.myapplication.Note
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseRepository(private val notesDAO: NotesDataSourceDAO) {

    suspend fun upsert(noteEntity: NoteEntity) = notesDAO.upsert(noteEntity)

    fun getNotes() = notesDAO.getNotes()

    fun getNote(id: UUID?) = notesDAO.getNote(id)

    suspend fun delete(e: NoteEntity) = notesDAO.delete(e)
}