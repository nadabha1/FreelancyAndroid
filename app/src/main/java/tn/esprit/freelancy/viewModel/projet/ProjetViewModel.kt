package tn.esprit.freelancy.viewModel.projet
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import tn.esprit.freelancy.model.projet.ApplicationRequest
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
    val _selectedProject = MutableStateFlow<Projet?>(null) // To store the selected project details
    val selectedProject: StateFlow<Projet?> = _selectedProject
    val userId: String? = sessionManager.getUserId()

    fun fetchProjects() {
        viewModelScope.launch {
            try {
                if (userId != null) {
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
                    _projectsIa.value = response

                }
                if (userId == null) {
                    Log.e("ProjetViewModel", "User ID is null or undefined")
                }
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
    fun fetchProjectById(projectId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.projetApi.getProjectById(projectId) // API call to fetch project
                _selectedProject.value = response // Store the fetched project
            } catch (e: Exception) {
                println("Error fetching project by ID: ${e.message}")
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
                val projectList = RetrofitClient.projetApi.getAllProjectsById(userId)
                _projects.value = projectList
            }
            else {
            val projectList = RetrofitClient.projetApi.getAllProjects()
            _projects.value = projectList
            }
        }
    }
    private fun loadProjectsIA() {
        // Fetch projects from your backend using Retrofit or another data source
        viewModelScope.launch {
            if (userId != null) {
                val projectList = RetrofitClient.projetApi.getOngoingProjects(userId)
                _projectsIa.value = projectList
            }

        }
    }

    fun applyForProject(projectId: String) {
        viewModelScope.launch {
            try {
                if (userId != null) {
                    val applicationRequest = ApplicationRequest(freelancer = userId, project = projectId,    status = "pending"  // Make sure status is a valid string
                    )

                    // Log the request for debugging purposes
                    println("Sending application request: $applicationRequest")

                    // Send the application request using Retrofit
                    val response = RetrofitClient.projetApi.createApplication(applicationRequest)

                    // Handle success response
                    println("Application submitted successfully: $response")
                } else {
                    println("User not logged in")
                }
            } catch (e: Exception) {
                // Handle error and provide more details
                if (e is HttpException) {
                    val errorResponse = e.response()?.errorBody()?.string()
                    println("Error applying for project: $errorResponse")
                } else {
                    println("Error applying for project: ${e.message}")
                }
            }
        }
    }

}
