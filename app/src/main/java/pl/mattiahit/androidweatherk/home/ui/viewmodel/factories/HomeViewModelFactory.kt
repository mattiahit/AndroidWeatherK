package pl.mattiahit.androidweatherk.home.ui.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.mattiahit.androidweatherk.home.ui.viewmodel.HomeViewModel
import javax.inject.Inject
import javax.inject.Provider

class HomeViewModelFactory @Inject constructor(private val homeViewModelProvider: Provider<HomeViewModel>): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeViewModelProvider.get() as T
    }
}