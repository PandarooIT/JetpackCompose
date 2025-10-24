package com.example.jetpackcompose.di.component

import androidx.activity.ComponentActivity
import com.example.jetpackcompose.di.module.ActivityModule
import com.example.jetpackcompose.di.scoped.ActivityScope
import com.example.jetpackcompose.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component (
    modules = [
        ActivityModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface ActivityComponent {
    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent, @BindsInstance activity: ComponentActivity): ActivityComponent
    }
    fun inject (activity: MainActivity)
}