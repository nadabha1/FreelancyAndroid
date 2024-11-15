package tn.esprit.freelancy.model

data class User(
    val email: String,
    val password: String,
    val username: String,
    val avatarUrl: String,
    val idUser: String
)
data class UpdateProfileRequest(
    val idUser: String,
    val email: String,       // Email de l'utilisateur
    val username: String   )