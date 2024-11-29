package tn.esprit.freelancy.model.user

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") var avatarUrl: String? = null
)
data class UserProfile1(
    @SerializedName("idUser") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String
)
data class UserProfileComplet(
    @SerializedName("id") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") var avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("role") val role:String? = null,
    @SerializedName("skills") val skills: List<String>? = null

)