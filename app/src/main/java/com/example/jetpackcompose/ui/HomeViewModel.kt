package com.example.jetpackcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.ExploringRepositoryImpl
import com.example.jetpackcompose.data.FeatureRepositoryImpl
import com.example.jetpackcompose.data.repo.ExploringRepository
import com.example.jetpackcompose.data.repo.FeatureRepository
import com.example.jetpackcompose.model.ExploreItem
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private fun loadFeatures() {
        viewModelScope.launch {
            _features.value = featureRepository.getFeatures()
        }
    }

    private fun loadExploreItems () {
        viewModelScope.launch {
            _exploreItems.value = exploringRepository.getExploreItems()
        }
    }
}