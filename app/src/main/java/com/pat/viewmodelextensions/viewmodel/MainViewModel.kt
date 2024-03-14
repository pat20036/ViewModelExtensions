package com.pat.viewmodelextensions.viewmodel

import androidx.lifecycle.viewModelScope
import com.pat.viewmodelextensions.viewmodel.UiAction.UpdateState
import com.pat.vm_extensions.viewmodel.BaseUiState
import com.pat.vm_extensions.viewmodel.StateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : StateViewModel<UiState, UiAction>() {

    override val state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)

    init {
        viewModelScope.launch {
            collectLatestAction {
                when (it) {
                    is UpdateState -> updateState(it.state)
                }
            }
        }
    }
}

sealed interface UiState : BaseUiState {

    data object Loading : UiState

    data object Loaded : UiState
}

sealed class UiAction {

    data class UpdateState(val state: UiState) : UiAction()

}