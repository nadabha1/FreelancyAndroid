package tn.esprit.freelancy.viewModel.projet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.esprit.freelancy.repository.ProjetRepository
import tn.esprit.freelancy.session.SessionManager

class ProjetViewModelFactory(private val repository: SessionManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjetViewModel::class.java)) {
            return ProjetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
