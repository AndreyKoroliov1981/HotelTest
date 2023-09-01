package com.korol.myapplication.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE>(initialState: STATE) : ViewModel() {
    protected val mutableStateFlow: MutableStateFlow<STATE> by lazy { MutableStateFlow(initialState) }
    val stateFlow: StateFlow<STATE> = mutableStateFlow.asStateFlow()
    protected val state: STATE get() = stateFlow.value

    protected val sideEffectSharedFlow = MutableSharedFlow<BaseSideEffect>()
    val sideEffect: SharedFlow<BaseSideEffect> = sideEffectSharedFlow.asSharedFlow()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    protected inline fun updateState(transform: STATE.() -> STATE) {
        mutableStateFlow.value = transform.invoke(state)
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(handler) { block(this) }

    protected fun onError(throwable: Throwable) {
        if (throwable !is CancellationException) {
            Napier.e(throwable.message.orEmpty(), throwable)
        }
    }
}
