package by.bsuir.myapplication


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.myapplication.MVI.MVIViewModel
import by.bsuir.myapplication.database.entity.DatabaseRepository
import by.bsuir.myapplication.database.entity.Mapper

import by.bsuir.myapplication.database.entity.NoteEntity
import by.bsuir.myapplication.database.entity.NotesMapper
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import java.util.UUID

interface NotesDataSource {
    fun getNotes(): Flow<List<Note>>
    fun getNote(id: UUID?): Flow<Note?>

    suspend fun upsert(note: Note)
    suspend fun delete(e: Note)
}

class RoomNotesDataSource(private val repository: DatabaseRepository) : NotesDataSource {

    override fun getNotes(): Flow<List<Note>> {

        return repository.getNotes().map {list -> list.map { NotesMapper.toDTO(it)}}
    }

    override fun getNote(id: UUID?): Flow<Note?> {
        return repository.getNote(id).map { it?.let { NotesMapper.toDTO(it) } }
    }

    override suspend fun upsert(note: Note) {
        repository.upsert(NotesMapper.toEntity(note))
    }

    override suspend fun delete(e: Note) {
        repository.delete(NotesMapper.toEntity(e))
    }
}

data class NoteUiState(
    val id: UUID = UUID.randomUUID(),
    val goal: String = "",
    val date: String = "",
    val temp: String = "",
    val condition: String = "",
    val maxwind: String ="",

    val isLoading: Boolean = false,
    val isNoteSaved: Boolean = false,
    val isNoteSaving: Boolean = false,
    val noteSavingError: String? = null
)


class AddEditViewModel(private val dataSource: NotesDataSource) : ViewModel() {

    private var noteId: String? = null

    private lateinit var note: Note

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init{
    }

    fun initViewModel(id: String?){
        if(id!=null)
            noteId = id.toString()
        if (noteId != null) {
            loadNote(UUID.fromString(noteId))
        }
    }

    private fun loadNote(noteId: UUID?) {

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
           // val result = dataSource.getNote(noteId).first()
            val result: Note? = dataSource.getNotes().first().find{it.id == noteId}
            if (result != null) {
                note = result
            }
            if (result == null) {
                _uiState.update { it.copy(isLoading = false) }
            } else {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        goal = result.goal,
                        date = result.date,
                        temp = result.temp,
                        maxwind = result.maxwind,
                        condition = result.condition
                    )
                }
            }
        }
    }


    fun saveNote() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isNoteSaving = true) }
                if (noteId != null) {
                    dataSource.upsert(
                       Note(
                            id = UUID.fromString(noteId),
                            goal = _uiState.value.goal,
                            date = _uiState.value.date,
                           temp = _uiState.value.temp,
                           maxwind = _uiState.value.maxwind,
                           condition = _uiState.value.condition
                        )
                    )
                } else {
                    dataSource.upsert(
                        Note(
                            id = UUID.randomUUID(),
                            goal = _uiState.value.goal,
                            date = _uiState.value.date,
                            temp = _uiState.value.temp,
                            maxwind = _uiState.value.maxwind,
                            condition = _uiState.value.condition
                        )
                    )
                }
                _uiState.update{it.copy(isNoteSaved = true)}

            } catch (e: Exception){
                _uiState.update { it.copy(noteSavingError = "Невозможно сохранить или изменить запись") }
            } finally {
                _uiState.update { it.copy(isNoteSaving = false) }
            }

        }


    }

    fun deleteNote() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isNoteSaving = true) }

                if(noteId!=null) {
                   dataSource.delete(note)
                }
                _uiState.update { it.copy(isNoteSaved = true) }
            }
            catch (e: Exception) {
                System.out.println(e)
                _uiState.update { it.copy(noteSavingError = "Упс... ошибочка") }
            }
            finally {
                _uiState.update { it.copy(isNoteSaving  = false) }
            }

        }
    }

    fun setNoteGoal(goal: String) {
        _uiState.update { it.copy(goal = goal) }
    }

    fun setNoteDate(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun setNoteTemp(temp:String){
        _uiState.update { it.copy(temp = temp) }
    }

    fun setNoteCondition(condition:String){
        _uiState.update { it.copy(condition = condition) }
    }

    fun setNoteMaxWind(maxwind:String){
        _uiState.update { it.copy(maxwind = maxwind) }
    }

}

data class NotesListUiState(
    var notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

class HomeViewModel(private val dataSource: NotesDataSource)
    : MVIViewModel<NotesListUiState, Unit, HomeEvent>(NotesListUiState(isLoading = true)) {
    public val notes = dataSource.getNotes()
    val _viewState = MutableStateFlow(NotesListUiState())
    private val notesLoadingItems = MutableStateFlow(0)

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            try {
                val notesList = dataSource.getNotes().toList()
                val notes = notesList.flatten()
                _viewState.value = _viewState.value.copy(notes = notes, isLoading = false)
            } catch (e: Throwable) {
                _viewState.value = _viewState.value.copy(error = e, isLoading = false)
            }
        }
    }
    fun deleteNote(note: Note) {
        event(HomeEvent.DeleteNoteEvent(note))
    }
    override suspend fun reduce(intent: Unit) {
        // do nothing
    }

    suspend fun reduce(intent: HomeEvent) {
        when (intent) {
            is HomeEvent.DeleteNoteEvent -> handleDeleteNoteEvent(intent.note)
        }
    }

    private suspend fun handleDeleteNoteEvent(note: Note) {
        withLoading {
            dataSource.delete(note)
            val notesList = dataSource.getNotes().toList()
            val notes = notesList.flatten()
            state { copy(notes = notes, isLoading = false) }
        }
    }

    private suspend fun withLoading(block: suspend () -> Unit) {
        try {
            state { copy(isLoading = true) }
            block()
        } finally {
            state { copy(isLoading = false) }
        }
    }
}

sealed class HomeEvent {
    data class DeleteNoteEvent(val note: Note) : HomeEvent()
}


//    var uiState = combine(notes, notesLoadingItems) { notes, loadingItems ->
//        NotesListUiState(
//            notes = notes.toList(),
//            isLoading = loadingItems > 0,
//            isError = false
//        )
//
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = NotesListUiState(isLoading = true)
//    )
//
//
//
//    private suspend fun withLoading(block: suspend () -> Unit) {
//        try {
//            addLoadingElement()
//            block()
//        }
//        finally {
//            removeLoadingElement()
//        }
//    }
//
//    private fun addLoadingElement() = notesLoadingItems.getAndUpdate { num -> num + 1 }
//    private fun removeLoadingElement() = notesLoadingItems.getAndUpdate { num -> num - 1 }
//    fun deleteNote(note: Note){
//        viewModelScope.launch {
//            withLoading {
//                dataSource.delete(note)
//            }
//        }
//    }
//}
