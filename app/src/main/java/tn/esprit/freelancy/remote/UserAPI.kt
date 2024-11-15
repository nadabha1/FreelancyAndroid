package tn.esprit.freelancy.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tn.esprit.freelancy.model.ApiResponse
import tn.esprit.freelancy.model.ForgotPasswordRequest
import tn.esprit.freelancy.model.GetUserIdRequest
import tn.esprit.freelancy.model.GetUserIdResponse
import tn.esprit.freelancy.model.LoginRequest
import tn.esprit.freelancy.model.LoginResponse
import tn.esprit.freelancy.model.Otp
import tn.esprit.freelancy.model.ResetPasswordRequest
import tn.esprit.freelancy.model.SignupRequest
import tn.esprit.freelancy.model.SignupResponse
import tn.esprit.freelancy.model.UpdateProfileRequest
import tn.esprit.freelancy.model.User
import tn.esprit.freelancy.model.UserProfile
import tn.esprit.freelancy.model.UserProfile1

interface UserAPI {

    @POST("user/getuser") // Endpoint pour récupérer le profil utilisateur
    suspend fun getUserId(@Body request: GetUserIdRequest): GetUserIdResponse
    @POST("user/login")
    fun login(@Body user: User): Call<User>
    @POST("user/login")
    suspend fun login2(@Body request: LoginRequest): LoginResponse
    @PATCH("user/update")
    suspend fun updateUserProfile(
        @Body updatedProfile: UserProfile1
    ): UserProfile

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") userId: String)

    @POST("user/getuser")
    suspend fun fetchUser(@Query("username") email: String): Response<UserProfile>
    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>
    @POST("user/forgot-password")
    suspend fun sendOtp(@Body request: ForgotPasswordRequest): Otp

    @POST("user/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>
}
