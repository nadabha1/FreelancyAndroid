package tn.esprit.freelancy.viewModel.projet
import android.app.AlertDialog
import android.content.Context
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
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

import org.json.JSONObject
import androidx.compose.runtime.State
import tn.esprit.freelancy.model.projet.ApplicationResponse
import tn.esprit.freelancy.model.projet.ApplicationStatusRequest
import tn.esprit.freelancy.model.projet.ApplicationStatusResponse
import tn.esprit.freelancy.model.projet.ApplicationStatusResponsem


class ProjetViewModel(private val sessionManager: SessionManager) : ViewModel() {
    val _projects = MutableStateFlow<List<Projet>>(emptyList())
    val projects: StateFlow<List<Projet>> = _projects
    val _applicationStatuses = MutableStateFlow<ApplicationStatusResponse?>(null)
    val applicationStatuses:StateFlow<ApplicationStatusResponse?> = _applicationStatuses
    val _projectsIa = MutableStateFlow<List<Project>>(emptyList())
    val projectsIa: StateFlow<List<Project>> = _projectsIa
    val _selectedProject = MutableStateFlow<Projet?>(null) // To store the selected project details
    val selectedProject: StateFlow<Projet?> = _selectedProject
     val _showError = MutableStateFlow("")
    val showError: StateFlow<String?> get() = _showError

    val _freelancer = MutableStateFlow<List<UserProfileComplet>>(emptyList()) // To store the selected project details
    val freelancer: StateFlow<List<UserProfileComplet>> = _freelancer
    val userId: String? = sessionManager.getUserId()


    private val _applicationStatus = MutableStateFlow<ApplicationStatusResponsem?>(null)
    val applicationStatus: StateFlow<ApplicationStatusResponsem?> = _applicationStatus

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
                println("Fetching project by ID: $projectId")
                val response = RetrofitClient.projetApi.getProjectById(projectId) // API call to fetch project
                _selectedProject.value = response // Store the fetched project
                try {
                    val freelancersList = RetrofitClient.projetApi.getFreelancersByProjectId(projectId)
                    _freelancer.value = freelancersList
                    println("Fetched project: $response")
                    println("Fetched freelancers: $freelancersList")
                } catch (e: Exception) {
                    println("Error fetching users by ID: ${e.message}")
                }
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



    fun applyForProject(projectId: String, context: Context) {
        viewModelScope.launch {
            try {
                if (userId != null) {
                    val applicationRequest = ApplicationRequest(
                        freelancer = userId,
                        project = projectId,
                        status = "Pending" // Make sure status is a valid string
                    )

                    // Log the request for debugging purposes
                    println("Sending application request: $applicationRequest")

                    // Send the application request using Retrofit
                    val response = RetrofitClient.projetApi.createApplication(applicationRequest)

                    // Handle success response
                    println("Application submitted successfully: $response")
                } else {
                    showErrorDialog(context, "User not logged in")
                }
            } catch (e: Exception) {
                // Handle error and provide more details
                if (e is HttpException) {
                    val errorResponse = e.response()?.errorBody()?.string()
                    // Extract only the message from the error JSON response
                    val errorMessage = extractErrorMessage(errorResponse)
                    showErrorDialog(context, errorMessage)
                } else {
                    showErrorDialog(context, "${e.message}")
                }
            }
        }
    }

    fun showErrorDialog(context: Context, errorMessage: String) {
        AlertDialog.Builder(context)
            .setTitle(" Error applying for project: ")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    fun acceptApplication(freelancerId: String, projectId: String) {
        // Appel API pour accepter la candidature
        viewModelScope.launch {
            try {
                val response = RetrofitClient.projetApi.acceptApplication(freelancerId, projectId)
                // Si la candidature est acceptée, on actualise l'UI
                fetchProjectById(projectId)
                fetchApplicationStatus(freelancerId,projectId)
            } catch (e: Exception) {
                _showError.value = "Error accepting application: ${e.message}"
            }
        }
    }

    fun extractErrorMessage(errorResponse: String?): String {
        return try {
            val jsonObject = JSONObject(errorResponse) // Convert the error response string into JSON
            jsonObject.getString("message") // Extract the "message" field from the JSON
        } catch (e: Exception) {
            "Unknown error" // Return a default message if parsing fails
        }
    }
    fun rejectApplication(freelancerId: String, projectId: String) {
        // Appel API pour rejeter la candidature
        viewModelScope.launch {
            try {
                val response = RetrofitClient.projetApi.rejectApplication(freelancerId, projectId)
                // Si la candidature est rejetée, on actualise l'UI
                fetchProjectById(projectId)
                fetchApplicationStatus(freelancerId,projectId)
            } catch (e: Exception) {
                _showError.value = "Error rejecting application: ${e.message}"
            }
        }
    }

        fun dismissError() {
            _showError.value = ""
        }

    private val _statusMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val statusMap: StateFlow<Map<String, String>> = _statusMap

    // Fonction pour récupérer le statut d'une application
    fun fetchApplicationStatus(freelancerId: String, projectId: String){
        viewModelScope.launch {
            try {
                val request = ApplicationStatusRequest(freelancerId, projectId)
                val response = RetrofitClient.projetApi.getApplicationStatus(request)
                _applicationStatuses.value = response
                _statusMap.value = _statusMap.value.toMutableMap().apply {
                    put(response.freelancer, response.status)
                }
            }
            catch (e: Exception) {
                _showError.value = "Error fetching application status: ${e.message}"
                println("Error fetching application status: ${e.message}")
            }
        }
    }

}



