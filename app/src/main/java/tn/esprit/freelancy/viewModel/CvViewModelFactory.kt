package tn.esprit.freelancy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.esprit.freelancy.repository.CvRepository

class CvViewModelFactory(private val repository: CvRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CvViewModel::class.java)) {
            return CvViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
