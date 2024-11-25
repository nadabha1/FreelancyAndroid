package tn.esprit.freelancy.repository

import retrofit2.Response
import retrofit2.http.Body
import tn.esprit.freelancy.model.GetUserIdResponse
import tn.esprit.freelancy.model.SignupData
import tn.esprit.freelancy.model.SignupRequest
import tn.esprit.freelancy.model.SignupResponse
import tn.esprit.freelancy.model.UpdateRoleRequest
import tn.esprit.freelancy.model.User
import tn.esprit.freelancy.model.UserProfile
import tn.esprit.freelancy.model.UserProfile1
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.remote.RetrofitClient.authService
import tn.esprit.freelancy.remote.UserAPI

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
    suspend fun updateUserProfile(updatedProfile: UserProfile1):UserProfile{
        return api.updateUserProfile(updatedProfile)
    }
    suspend fun signup(username: String, email: String,role: String, password: String): SignupResponse {
        val response = authService.signup(SignupRequest(username, email, password,role))
        if (!response.isSuccessful) {
            throw Exception("Signup failed: ${response.errorBody()?.string()}")
        }
        return response.body() ?: throw Exception("Empty response from server")
    }
    suspend fun updateRole(role: String, userId: String): Response<Any> {
        val request = UpdateRoleRequest(role = role, userId = userId)
        return api.updateUserRole(request)
    }



}