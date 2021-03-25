package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo

/**
 * This class is responsible to create a instance of ViewModel.
 * As the ViewModel have dependencies and will be tested later
 * it needs its own ViewModelProvider.
 * Factory and passed dependency through ViewModel constructor and
 * give value to the ViewModelProvider.
 */
class AdhanViewModelFactory(private val repo: AdhanRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdhanViewModel(repo) as T
    }


}