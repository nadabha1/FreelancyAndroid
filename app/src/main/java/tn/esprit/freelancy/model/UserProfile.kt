package tn.esprit.freelancy.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("idUser") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") var avatarUrl: String? = null
)
data class UserProfile1(
    @SerializedName("idUser") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)
