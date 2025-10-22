package com.example.jetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.HomeRepository
import com.example.jetpackcompose.state.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel (
    private val repo: HomeRepository = HomeRepository()
) : ViewModel() {
    private val _features = MutableStateFlow(UIState<List<String>>(isLoading = true))
    val features: StateFlow<UIState<List<String>>> = _features.asStateFlow()

    fun loadFeatures() {
        viewModelScope.launch {
            _features.value = UIState(isLoading = true)
            val result = runCatchingWithRetry(tries = 3, baseDelayMs = 300) {
                repo.fetchFeatures()
            }
            _features.value = result.fold(
                onSuccess = { UIState(data = it) },
                onFailure = { UIState(error = it.message ?: "Unknown error") }
            )
        }
    }

    private suspend fun <T> runCatchingWithRetry(
        tries: Int,
        baseDelayMs: Long,
        block: suspend () -> T
    ): Result<T> {
        var last: Throwable? = null
        repeat(tries) { attempt ->
            try { return Result.success(block()) }
            catch (e: CancellationException) { throw e }
            catch (e: Throwable) {
                last = e
                delay(baseDelayMs shl attempt) // backoff 300/600/1200
            }
        }
        return Result.failure(last ?: RuntimeException("Unknown"))
    }
}