package tn.esprit.freelancy.model.user

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
