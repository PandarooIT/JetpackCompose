package com.example.jetpackcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.ExploringRepositoryImpl
import com.example.jetpackcompose.data.FeatureRepositoryImpl
import com.example.jetpackcompose.data.repo.ExploringRepository
import com.example.jetpackcompose.data.repo.FeatureRepository
import com.example.jetpackcompose.model.ExploreItem
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val featureRepository: FeatureRepository,
    private val exploringRepository: ExploringRepository
) : ViewModel() {

    private val _features = MutableStateFlow<List<Feature>>(emptyList())
    val features: StateFlow<List<Feature>> = _features.asStateFlow()

    private val _exploreItems = MutableStateFlow<List<ExploreItem>>(emptyList())
    val exploreItems : StateFlow<List<ExploreItem>> = _exploreItems.asStateFlow()

    init {
        loadFeatures()
        loadExploreItems()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadFeatures() {
        intervalFlow(periodMs = 5000, initialDelayMs = 0)
            .onStart { emit(Unit) }
            .mapLatest {
                featureRepository.getFeatures()
            }
            .catch { }
            .onEach { _features.value = it }
            .launchIn(viewModelScope)
    }

    fun intervalFlow(periodMs: Long, initialDelayMs: Long = 0): Flow<Unit> = flow {
        if (initialDelayMs > 0) delay(initialDelayMs)
        while (currentCoroutineContext().isActive) {
            emit(Unit)
            delay(periodMs)
        }
    }

    private fun loadExploreItems () {
        viewModelScope.launch {
            _exploreItems.value = exploringRepository.getExploreItems()
        }
    }
}