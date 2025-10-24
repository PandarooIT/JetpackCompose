package com.example.jetpackcompose.data.repo

import com.example.jetpackcompose.model.ExploreItem
import com.example.jetpackcompose.model.Feature

interface ExploringRepository {
    suspend fun getExploreItems(): List<ExploreItem>
}