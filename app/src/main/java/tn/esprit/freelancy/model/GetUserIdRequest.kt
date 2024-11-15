package tn.esprit.freelancy.model

data class GetUserIdRequest(
    val username: String // Nom d'utilisateur envoyé dans le corps de la requête
)
data class GetUserIdResponse(
    val id: String,          // Correspond au champ `_id` de l'API
    val email: String,       // Email de l'utilisateur
    val username: String   )


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
