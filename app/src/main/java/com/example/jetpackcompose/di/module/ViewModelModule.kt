package com.example.jetpackcompose.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackcompose.DaggerViewModelFactory
import com.example.jetpackcompose.di.scoped.ViewModelKey
import com.example.jetpackcompose.ui.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindFeatureVm(vm: HomeViewModel): ViewModel

    @Binds
    abstract fun bindVmFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}