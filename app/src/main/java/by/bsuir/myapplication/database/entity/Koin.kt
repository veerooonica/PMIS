package by.bsuir.myapplication.database.entity

import by.bsuir.myapplication.AddEditViewModel
import by.bsuir.myapplication.HomeViewModel
import by.bsuir.myapplication.NotesDataSource
import by.bsuir.myapplication.RoomNotesDataSource
import by.bsuir.myapplication.WeatherViewModel
import by.bsuir.myapplication.api.ApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


fun provideDao(db: MyDatabase) = db.notesDAO()

val databaseModule = module {
    single { MyDatabase.get(get())  }
    single { provideDao(db = get()) }
    single {  ApiService.create() }
    factory<DatabaseRepository> {DatabaseRepository(notesDAO = get())}
    factory<NotesDataSource> { RoomNotesDataSource(repository = get()) }
    viewModel() { HomeViewModel(dataSource = get()) }
    viewModel() {AddEditViewModel(dataSource = get())}
    viewModel() {WeatherViewModel(apiService = get(), dataSource = get())}
}

val appModule = module {
    includes(databaseModule)
}

