package tn.esprit.freelancy.model

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val avatarUrl: String? = "dfghjkl"
)

data class SignupResponse(
    val message: String,
    val username: String,
    val email: String,
    val password: String,
    val success: Boolean
)
