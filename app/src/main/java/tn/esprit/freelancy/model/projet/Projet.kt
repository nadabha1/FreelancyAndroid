package tn.esprit.freelancy.model.projet

data class Projet(
    val _id: String? = null,
    val title: String,
    val description: String,
    val technologies: String,
    val budget: String,
    val duration: String, // Use String to handle date formatting
    val status: String,
    val userId: String
)
data class Project(
    val id: String? = null,
    val title: String,
    val description: String,
    val technologies: String,
    val budget: String,
    val status: String,
    val duration: String,
    val score: Double
)

data class ProjectResponse(
    val ranked_projects: List<Project>
)
