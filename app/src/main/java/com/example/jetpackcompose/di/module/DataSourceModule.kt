package com.example.jetpackcompose.di.module

import com.example.jetpackcompose.data.ExploringRepositoryImpl
import com.example.jetpackcompose.data.FeatureRepositoryImpl
import com.example.jetpackcompose.data.repo.ExploringRepository
import com.example.jetpackcompose.data.repo.FeatureRepository
import com.example.jetpackcompose.di.scoped.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class DataSourceModule {
    @Binds
    @ActivityScope
    abstract fun bindExploringRepository(impl: ExploringRepositoryImpl): ExploringRepository

    @Binds
    @ActivityScope
    abstract fun bindFeatureRepository(impl: FeatureRepositoryImpl): FeatureRepository
}