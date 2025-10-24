package com.example.jetpackcompose.data

import com.example.jetpackcompose.R
import com.example.jetpackcompose.data.repo.FeatureRepository
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeatureRepositoryImpl @Inject constructor() : FeatureRepository {
    override suspend fun getFeatures(): List<Feature> = withContext(Dispatchers.IO) {
        delay(500) // giả lập tải dữ liệu
        listOf(
            Feature(1, R.drawable.ic_background_motorist, "Road & Valet Assist"),
            Feature(2, R.drawable.ic_co_driver, "Co-Driver Alerts"),
            Feature(3, R.drawable.ic_quick_car_valuation, "Quick Car Valuation"),
            Feature(4, R.drawable.ic_insurance, "Insurance Quote"),
            Feature(5, R.drawable.ic_exchange, "Used Vehicles"),
            Feature(6, R.drawable.ic_traffic_camera, "Traffic Cameras"),
            Feature(7, R.drawable.ic_background_motorist, "Traffic Incidents"),
            Feature(8, R.drawable.ic_background_motorist, "Checkpoint"),
            Feature(9, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
            Feature(10, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
            Feature(11, R.drawable.ic_co_driver, "Traffic Hot Positions"),
            Feature(12, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
            Feature(13, R.drawable.ic_insurance, "Traffic Hot Positions"),
            Feature(14, R.drawable.ic_exchange, "Traffic Hot Positions"),
            Feature(15, R.drawable.ic_traffic_camera, "Traffic Hot Positions"),
        )
    }
}