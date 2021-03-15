package io.github.kabirnayeem99.alarmforsalat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo

class AdhanViewModelFactory(private val repo: AdhanRepo, var city: String, var country: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdhanViewModel(repo, city, country) as T
    }


}