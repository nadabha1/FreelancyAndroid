package tn.esprit.freelancy.model.user

data class User(
    val email: String,
    val password: String,
    val username: String,
    val avatarUrl: String,
    val _id: String,
    val role: String,
    val otp: String
)
data class UpdateProfileRequest(
    val idUser: String,
    val email: String,       // Email de l'utilisateur
    val username: String   )
