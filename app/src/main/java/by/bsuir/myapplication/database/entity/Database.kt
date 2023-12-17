package by.bsuir.myapplication.database.entity


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bsuir.myapplication.database.entity.Constants.NOTE_DATABASE


@Database(entities = [NoteEntity::class], version = 2)
@TypeConverters(Converter::class)
abstract class MyDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: MyDatabase? = null

        fun get(context: Context): MyDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, MyDatabase::class.java, NOTE_DATABASE).fallbackToDestructiveMigration().build()
            }
            return INSTANCE as MyDatabase
        }

    }
    abstract fun notesDAO(): NotesDataSourceDAO

}


