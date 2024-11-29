package tn.esprit.freelancy.viewModel.projet
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.ProjectResponse
import tn.esprit.freelancy.model.projet.Projet
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager

class ProjetViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val _projects = MutableStateFlow<List<Projet>>(emptyList())
    val projects: StateFlow<List<Projet>> = _projects
    val _projectsIa = MutableStateFlow<List<Project>>(emptyList())
    val projectsIa: StateFlow<List<Project>> = _projectsIa

     val userId: String? = sessionManager.getUserId()
    val userid = sessionManager.getSession()?.id

    fun fetchProjects() {
        viewModelScope.launch {
            try {
                if (userId != null) {
                    // Call the POST method with the userId in the request body
                   val response = RetrofitClient.projetApi.getAllProjectsById(userId)

                }
            } catch (e: Exception) {
                println("Error fetching projects: ${e.message}")
            }
        }
    }
    fun fetchProjectByIa() {
        viewModelScope.launch {
            try {
                if (userId != null) {
                    val response = RetrofitClient.projetApi.getOngoingProjects(userId)
                    if (response != null) {
                        println("Fetched projects: $response")
                        _projectsIa.value = response}}
            } catch (e: Exception) {
                println("Error fetching projects: ${e.message}")
            }
        }
    }
    fun addProject(projet: Projet) {
        viewModelScope.launch {
            try {
                RetrofitClient.projetApi.addProject(projet)
               // fetchProjects() // Refresh project list after adding
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    init {
        loadProjects()
    }

    private fun loadProjects() {
        // Fetch projects from your backend using Retrofit or another data source
        viewModelScope.launch {
            if (userId != null) {
                println("baaaaaaaaaad: $userId")
                val projectList = RetrofitClient.projetApi.getAllProjectsById(userId)
                _projects.value = projectList
            }
            else {
            val projectList = RetrofitClient.projetApi.getAllProjects()
            _projects.value = projectList
            }
        }
    }
}
