package tn.esprit.freelancy.model

data class UpdateRoleRequest(
    val role: String,
    val userId: String
)
data class UserProfileUpdateRequest(
    val username:String,
    val dateOfBirth: String,
    val country: String,
    val avatarUrl: String
)
