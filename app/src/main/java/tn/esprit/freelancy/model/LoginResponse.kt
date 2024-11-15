package tn.esprit.freelancy.model

data class LoginResponse(
    val access_token: String // Correspond exactement à la clé renvoyée par l'API NestJS
)