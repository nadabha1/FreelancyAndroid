package tn.esprit.freelancy.remote.projet

import retrofit2.http.*
import tn.esprit.freelancy.model.projet.ApplicationRequest
import tn.esprit.freelancy.model.projet.ApplicationResponse
import tn.esprit.freelancy.model.projet.Notification
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.ProjectResponse
import tn.esprit.freelancy.model.projet.Projet

interface ProjetAPI {
    @POST("projet/add")
    suspend fun addProject(@Body projet: Projet): Projet

    @GET("projet/get")
    suspend fun getAllProjects(): List<Projet>

    @POST("projet/user2")
    suspend fun getAllProjectsById(@Query("userId") userId: String): List<Projet>

    @GET("projet/{id}")
    suspend fun getProjectById(@Path("id") id: String): Projet // Fetch project by ID

    @GET("projet/user")
    suspend fun getAllProjectsByIdd(@Query("id") userId: String): List<Projet>


    @PATCH("projet/update")
    suspend fun updateProject(@Body projet: Projet): Projet

    @DELETE("projet/delete")
    suspend fun deleteProject(@Query("id") id: String): Projet

    @POST("/project-filter/filterPer2")
    suspend fun getOngoingProjects(@Query("userId") userId: String): List<Project>
    @POST("application")
    suspend fun createApplication(@Body applicationRequest: ApplicationRequest): ApplicationResponse

    @GET("notifications/entrepreneur/{entrepreneurId}")
    suspend fun getNotifications(
        @Path("entrepreneurId") entrepreneurId: String
    ): List<Notification>

    @PATCH("notifications/{notificationId}")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: String
    ): Notification


}
