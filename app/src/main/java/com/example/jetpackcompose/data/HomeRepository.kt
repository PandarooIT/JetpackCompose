package com.example.jetpackcompose.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import kotlin.random.Random

class HomeRepository {
    suspend fun fetchFeatures(): List<String> = withContext(Dispatchers.IO) {
        delay(800)
        if (Random.nextFloat() < 0.15f) error("Network error (features)!")
        listOf(
            "Road & Valet Assist", "Co-Driver Alerts", "Quick Car Valuation",
            "Insurance Quote", "Used Vehicles", "Traffic Cameras",
            "Traffic Incidents", "Checkpoint", "Traffic Hot Positions",
            "Road & Valet Assist", "Co-Driver Alerts", "Quick Car Valuation"
        )
    }
}