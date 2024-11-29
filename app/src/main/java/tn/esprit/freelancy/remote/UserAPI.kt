package tn.esprit.freelancy.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import tn.esprit.freelancy.model.user.ApiResponse
import tn.esprit.freelancy.model.user.ForgotPasswordRequest
import tn.esprit.freelancy.model.user.GetRoleIdResponse
import tn.esprit.freelancy.model.user.GetUserIdRequest
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.GetUserResponsetest
import tn.esprit.freelancy.model.user.LoginRequest
import tn.esprit.freelancy.model.user.LoginResponse
import tn.esprit.freelancy.model.user.Otp
import tn.esprit.freelancy.model.user.ResetPasswordRequest
import tn.esprit.freelancy.model.user.SignupRequest
import tn.esprit.freelancy.model.user.SignupResponse
import tn.esprit.freelancy.model.user.UpdateRoleRequest
import tn.esprit.freelancy.model.user.UserProfile
import tn.esprit.freelancy.model.user.UserProfile1
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.model.user.UserProfileUpdateRequest

interface UserAPI {

    @POST("user/getuser") // Endpoint pour récupérer le profil utilisateur
    suspend fun getUserId(@Body request: GetUserIdRequest): GetUserResponsetest
    @POST("user/login")
    suspend fun login2(@Body request: LoginRequest): LoginResponse
    @PATCH("user/update")
    suspend fun updateUserProfile(
        @Body updatedProfile: UserProfile1
    ): UserProfile

    @GET("user/profile") // Replace with your actual endpoint
    suspend fun getUserProfile(@Query("email") email: String): UserProfileComplet
    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") userId: String)
    @POST("user/getuser")
    suspend fun fetchUser(@Query("username") email: String): Response<UserProfile>
    @POST("user/get-id")
    suspend fun fetchUserId(@Query("username") email: String): String
    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>
    @POST("user/forgot-password")
    suspend fun sendOtp(@Body request: ForgotPasswordRequest): Otp

    @POST("user/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ApiResponse>

    @PUT("user/update-role")
    suspend fun updateUserRole(@Body request: UpdateRoleRequest): Response<Any>

    @PATCH("user/createProfile")
    suspend fun createUserProfile(@Body profile: UserProfileUpdateRequest): Response<Any>


    @POST("user/getIdRole")
    suspend fun getRoleName(@Body request: GetUserIdResponse): GetRoleIdResponse
}
