package by.bsuir.myapplication

import androidx.room.ColumnInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID


data class Note(
    var goal: String,
    var date: String,
    var temp: String,
    var condition: String,
    var maxwind: String,
    val id: UUID = UUID.randomUUID()
)