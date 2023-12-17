package by.bsuir.myapplication.database.entity

import androidx.room.TypeConverter
import java.util.UUID

class Converter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID? {
        return UUID.fromString(string)
    }
}