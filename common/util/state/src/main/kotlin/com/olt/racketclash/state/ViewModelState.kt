package com.olt.racketclash.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ViewModelState<S>(
    initialState: S
) : ViewModel() {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    fun updateState(block: S.() -> S) =
        _state.update(block)

    fun onIO(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = Dispatchers.IO, block = block)

    fun onDefault(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = Dispatchers.Default, block = block)

    fun onMain(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = Dispatchers.Main, block = block)
}