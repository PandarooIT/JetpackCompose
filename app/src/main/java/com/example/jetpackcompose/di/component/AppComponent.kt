package com.example.jetpackcompose.di.component

import com.example.jetpackcompose.di.module.AppModule
import dagger.Component

@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
}