package tn.esprit.freelancy.model.user

data class GetUserIdRequest(
    val username: String // Nom d'utilisateur envoyé dans le corps de la requête
)
data class GetUserResponse(
    val _id: String?, // _id est maintenant nullable
    val email: String,       // Email de l'utilisateur
    val username: String,
    val role: String,          // Correspond au champ `_id` de l'API
    val avatarUrl: String       // Email de l'utilisateur

)
data class GetUserIdResponse(
    val id:String

)
data class GetRoleIdResponse(
    val idRole:String

)
data class GetUserResponsetest(
    val user: User

)
data class ForgotPasswordRequest(
    val username: String
)

data class ApiResponse(
    val message: String,
    val success: Boolean,
    val otp:String="123"

)
data class Otp(
    val otp:String="123"

)


data class ResetPasswordRequest(
    val username: String,
    val otp: String ,
    val newPassword: String
)
data class LoginResult(
    val success: Boolean,
    val message: String? = null
)
