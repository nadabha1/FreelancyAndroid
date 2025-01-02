package tn.esprit.freelancy.model.user

import retrofit2.http.Part

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String="Freelancer",
    val avatarUrl: String? = "dfghjkl"
)

data class SignupResponse(
    @Part("msg") val message: String,
    @Part("username") val username: String,
    @Part("email")  val email: String,
    val _id: String,

    val password: String,
    val success: Boolean
)
data class SignupData(
    @Part("username") val username: String,
    @Part("email")  val email: String,
    @Part("password") val password: String = "",
    @Part("role") val role: String = "",
    @Part("avatarUrl") val profileImage: String? = null // URI de l'image sélectionnée
)