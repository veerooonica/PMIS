package by.bsuir.myapplication.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import by.bsuir.myapplication.database.entity.Constants.NOTE_TABLE
import by.bsuir.myapplication.database.entity.Constants.WEATHER_TABLE
import java.util.UUID

@Entity(tableName = NOTE_TABLE)
data class NoteEntity(
    @ColumnInfo(name = "goal")
    var goal: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "temp")
    var temp: String,
    @ColumnInfo(name="condition")
    var condition: String,
    @ColumnInfo(name="maxwind")
    var maxwind: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID
)