package com.example.jetpackcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.FeatureRepository
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val repo: FeatureRepository = FeatureRepository()
) : ViewModel() {

    private val _features = MutableStateFlow<List<Feature>>(emptyList())
    val features: StateFlow<List<Feature>> = _features.asStateFlow()

    init {
        loadFeatures()
    }

    private fun loadFeatures() {
        viewModelScope.launch {
            _features.value = repo.getFeatures()
        }
    }
}