package com.example.jetpackcompose.data.repo

import androidx.lifecycle.LiveData
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.flow.StateFlow

interface FeatureRepository {
    suspend fun getFeatures(): List<Feature>
}