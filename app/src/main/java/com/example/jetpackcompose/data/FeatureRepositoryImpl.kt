package com.example.jetpackcompose.data

import com.example.jetpackcompose.R
import com.example.jetpackcompose.data.repo.FeatureRepository
import com.example.jetpackcompose.model.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class FeatureRepositoryImpl @Inject constructor() : FeatureRepository {
    override suspend fun getFeatures(): List<Feature> = withContext(Dispatchers.IO) {
        delay(500)
        getFeaturesRandom(Random.nextInt(0, 3))
    }

    fun getFeaturesRandom(num: Int): List<Feature> {
        return when (num) {
            0 -> listOf(
                Feature(1, R.drawable.ic_background_motorist, "0"),
                Feature(2, R.drawable.ic_co_driver, "List 1"),
                Feature(3, R.drawable.ic_quick_car_valuation, "Quick Car Valuation"),
                Feature(4, R.drawable.ic_insurance, "Insurance Quote"),
                Feature(5, R.drawable.ic_exchange, "Used Vehicles"),
                Feature(6, R.drawable.ic_traffic_camera, "Traffic Cameras"),
                Feature(7, R.drawable.ic_background_motorist, "Traffic Incidents"),
                Feature(8, R.drawable.ic_background_motorist, "Checkpoint"),
                Feature(9, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
                Feature(10, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
            )
            1 -> listOf(
                Feature(1, R.drawable.ic_background_motorist, "1"),
                Feature(2, R.drawable.ic_co_driver, "List 2"),
                Feature(3, R.drawable.ic_quick_car_valuation, "Quick Car Valuation"),
                Feature(4, R.drawable.ic_insurance, "Insurance Quote"),
                Feature(5, R.drawable.ic_exchange, "Used Vehicles"),
            )
            2 -> listOf(
                Feature(1, R.drawable.ic_background_motorist, "2"),
                Feature(2, R.drawable.ic_co_driver, "List 2"),
                Feature(3, R.drawable.ic_quick_car_valuation, "Quick Car Valuation"),
                Feature(4, R.drawable.ic_insurance, "Insurance Quote"),
                Feature(5, R.drawable.ic_exchange, "Used Vehicles"),
                Feature(6, R.drawable.ic_traffic_camera, "Traffic Cameras"),
                Feature(7, R.drawable.ic_background_motorist, "Traffic Incidents"),
                Feature(8, R.drawable.ic_background_motorist, "Checkpoint"),
                Feature(9, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
                Feature(10, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
                Feature(11, R.drawable.ic_background_motorist, "Road & Valet Assist"),
                Feature(12, R.drawable.ic_co_driver, "Co-Driver Alerts"),
                Feature(13, R.drawable.ic_quick_car_valuation, "Quick Car Valuation"),
                Feature(14, R.drawable.ic_insurance, "Insurance Quote"),
                Feature(15, R.drawable.ic_exchange, "Used Vehicles"),
                Feature(16, R.drawable.ic_traffic_camera, "Traffic Cameras"),
                Feature(17, R.drawable.ic_background_motorist, "Traffic Incidents"),
                Feature(18, R.drawable.ic_background_motorist, "Checkpoint"),
                Feature(19, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
                Feature(20, R.drawable.ic_background_motorist, "Traffic Hot Positions"),
            )
            else -> emptyList()
        }
    }


}