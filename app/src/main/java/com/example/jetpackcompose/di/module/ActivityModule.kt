package com.example.jetpackcompose.di.module

import dagger.Module

@Module (
    includes = [
        DataSourceModule::class,
        ViewModelModule::class
    ]
)
class ActivityModule {
}