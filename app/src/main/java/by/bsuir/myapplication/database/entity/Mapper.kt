package by.bsuir.myapplication.database.entity

import by.bsuir.myapplication.Note

interface Mapper<T, R> {
    fun toDTO(from: NoteEntity): R
    fun toEntity(from: R): T
}

object NotesMapper : Mapper<NoteEntity, Note> {
    override fun toDTO(from: NoteEntity): Note {
        return Note(
            goal = from.goal,
            date = from.date,
            temp = from.temp,
            maxwind = from.maxwind,
            condition = from.condition,
            id = from.id,
        )
    }

    override fun toEntity(from: Note): NoteEntity {
        return NoteEntity(
            goal = from.goal,
            date = from.date,
            temp = from.temp,
            maxwind = from.maxwind,
            condition = from.condition,
            id = from.id,
        )
    }

}