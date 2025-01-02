package tn.esprit.freelancy.model.user

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val _id: String,
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
    @SerializedName("skills") val skills: String?= null
)
data class UserProfileFireBase(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val skills: String = "",
    val role: String = ""
) {
    // Constructeur sans argument nécessaire pour Firebase
    constructor() : this("", "", "", "")
}
data class UserProfileCompletRes(
    @SerializedName("_id") val idUser: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatarUrl") var avatarUrl: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("role") val role:String? = null,
    @SerializedName("skills") val skills: String?= null

)
data class UpdateSkillsRequest(
    val skills: List<String>
)
