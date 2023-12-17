package by.bsuir.myapplication.MVI

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.ParametersDefinition

interface Container<S, I, E> {
    fun CoroutineScope.subscribe(onEvent: suspend (E) -> Unit)
    val states: StateFlow<S>
    fun intent(intent: I)
}

abstract class MVIViewModel<S, I, E>(initial: S) : ViewModel(), Container<S, I, E> {

    private val _states: MutableStateFlow<S> = MutableStateFlow(initial) // Создаем флоу для стейтов и инициализируем его начальным значением
    private val _events = Channel<E>(Channel.UNLIMITED) // Создаем канал событий

    // должны быть final!
    final override val states = _states.asStateFlow() // Приводим флоу стейтов к неизменяемому состоянию
    final override fun intent(intent: I){viewModelScope.launch{reduce(intent)}} // Вызываем функцию reduce в корутине

    final override fun CoroutineScope.subscribe(onEvent: suspend (E) -> Unit) {
        launch{
            for (event in _events){
                onEvent(event) // Запускаем цикл получения событий и вызываем колбэк onEvent для каждого события
            }
        }
        onSubscribe()
    }

    protected fun event(event: E){
        viewModelScope.launch{
            _events.send(event) // Отправляем событие в канал
        }
    }
    protected fun state(block: S.() -> S){
        val newState = _states.value.block() // Обновляем стейт, вызывая переданный блок
        _states.value = newState // Присваиваем новое значение флоу стейтов
    }
    protected open fun CoroutineScope.onSubscribe() = Unit // Оставляем функцию onSubscribe пустой, чтобы ее можно было переопределить в дочерних классах
    protected abstract suspend fun reduce(intent: I) // Объявляем абстрактную функцию reduce, которую должны реализовать дочерние классы

    @Composable
    fun <S, A> Container<S, *, A>.subscribe(onEvent: suspend (A) -> Unit): State<S> { // 1
        val state = states.collectAsStateWithLifecycle()
        val lifecycle = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Main.immediate) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    subscribe(onEvent)
                }
            }
        }
        return state
    }

    // это для коина
    @Composable
    inline fun <reified T, S, I, E> container( // 2
        noinline params: ParametersDefinition? = null,
    ): Container<S, I, E> where T : Container<S, I, E>, T : ViewModel = getViewModel<T>(parameters = params)

    // использовать так:
    //val container = container<CatDetailsViewModel, _, _, _> { parametersOf(id) }////////////////////////////ИСПОЛЬЗОВАТЬ ТАК////////////////
}