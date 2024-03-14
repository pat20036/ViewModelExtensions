package com.pat.vm_extensions.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@SuppressLint("ComposableNaming")
abstract class StateViewModel<STATE_TYPE : BaseUiState, ACTION_TYPE> : ViewModel() {

    abstract val state: MutableStateFlow<STATE_TYPE>

    private val _action: MutableSharedFlow<ACTION_TYPE> = MutableSharedFlow(replay = 0)

    @Composable
    fun runWithState(action: @Composable (STATE_TYPE) -> Unit) {
        action(state.collectAsStateWithLifecycle().value)
    }

    fun updateState(value: STATE_TYPE) {
        state.update {
            value
        }
    }

    suspend fun emitAction(value: ACTION_TYPE) {
        _action.emit(value)
    }

    suspend fun collectLatestAction(uiAction: (ACTION_TYPE) -> Unit) {
        _action.collectLatest {
            uiAction(it)
        }
    }
}

