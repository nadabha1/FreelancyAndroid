package tn.esprit.freelancy.repository

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import tn.esprit.freelancy.model.user.ApiResponse
import tn.esprit.freelancy.model.user.ApiResponse2
import tn.esprit.freelancy.model.user.SignupRequest
import tn.esprit.freelancy.model.user.SignupResponse
import tn.esprit.freelancy.model.user.UpdateRoleRequest
import tn.esprit.freelancy.model.user.UpdateSkillsRequest
import tn.esprit.freelancy.model.user.UserProfile
import tn.esprit.freelancy.model.user.UserProfile1
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.remote.RetrofitClient.authService
import tn.esprit.freelancy.remote.UserAPI
import java.io.File

class AuthRepository(private val api: UserAPI) {
    fun login(email: String, password: String): Boolean {
        // Simule une authentification (remplacez par une requête réseau)
        return email == "test@example.com" && password == "password"
    }

    suspend fun fetchUser(email: String): Response<UserProfile> {
        println("Calling API for user: $email")
        return api.fetchUser(email)
    }


    suspend fun fetchUserId(email: String): String {
        println("fetch for user: $email")
        return api.fetchUserId(email)
    }


    suspend fun signup(
        username: String,
        email: String,
        role: String,
        password: String
    ): SignupResponse {
        val response = authService.signup(SignupRequest(username, email, password, role))
        if (!response.isSuccessful) {
            throw Exception("Signup failed: ${response.errorBody()?.string()}")
        }
        return response.body() ?: throw Exception("Empty response from server")
    }

    suspend fun updateRole(role: String, userId: String): Response<Any> {
        val request = UpdateRoleRequest(role = role, userId = userId)
        return api.updateUserRole(request)
    }

    suspend fun fetchUserProfile(email: String): UserProfileComplet? {
        return try {
            val response = RetrofitClient.authService.getUserProfile(email)
            if (response.idUser.isEmpty()) {
                println("l'id est : "+response.idUser)
                null // If user idUser is missing, treat as invalidUser
            } else {
                println("l'idUser est : "+response.idUser)
                response
            }
        } catch (e: Exception) {
            println("Error fetching user profile: ${e.message}")
            null // Handle API errors gracefully
        }
    }

    suspend fun updateUserSkills(userId: String, skills: List<String>): ApiResponse2 {
        val request = UpdateSkillsRequest(skills) // Wrap skills in the request object
        val response = authService.updateSkills(userId, request)
        println("Response from server update skills: $response")
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response from server")
        } else {
            throw Exception("Failed to update skills: ${response.message()}")
        }
    }




}

