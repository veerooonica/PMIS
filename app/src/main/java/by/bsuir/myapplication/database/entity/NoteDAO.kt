package by.bsuir.myapplication.database.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.bsuir.myapplication.database.entity.Constants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface NotesDataSourceDAO {
    @Query("SELECT * From $NOTE_TABLE")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * From $NOTE_TABLE Where 'id'=:id")
    fun getNote(id: UUID?): Flow<NoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: NoteEntity)

    @Delete
    suspend fun delete(e: NoteEntity)
}

