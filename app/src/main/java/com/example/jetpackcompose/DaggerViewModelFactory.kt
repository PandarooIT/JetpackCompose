package com.example.jetpackcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            creator = creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
        }
        requireNotNull(creator) { "Unknown model class $modelClass" }
        return creator.get() as T
    }
}