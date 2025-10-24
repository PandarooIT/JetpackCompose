package com.example.jetpackcompose.data

import com.example.jetpackcompose.R
import com.example.jetpackcompose.data.repo.ExploringRepository
import com.example.jetpackcompose.model.ExploreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExploringRepositoryImpl @Inject constructor() : ExploringRepository{
    override suspend fun getExploreItems() : List<ExploreItem> = withContext(Dispatchers.IO) {
        delay(500)
        listOf(
            ExploreItem(1, R.drawable.ic_background_motorist),
            ExploreItem(2, R.drawable.ic_co_driver),
            ExploreItem(3, R.drawable.ic_traffic_camera),
            ExploreItem(4, R.drawable.ic_insurance),
            ExploreItem(5, R.drawable.ic_quick_car_valuation),
            ExploreItem(6, R.drawable.ic_exchange),
            ExploreItem(7, R.drawable.ic_launcher_background),
            ExploreItem(8, R.drawable.ic_background_motorist),
        )
    }
}