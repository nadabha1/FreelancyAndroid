package tn.esprit.freelancy.remote.projet

import retrofit2.http.*
import tn.esprit.freelancy.model.projet.Project
import tn.esprit.freelancy.model.projet.ProjectResponse
import tn.esprit.freelancy.model.projet.Projet

interface ProjetAPI {
    @POST("projet/add")
    suspend fun addProject(@Body projet: Projet): Projet

    @GET("projet/get")
    suspend fun getAllProjects(): List<Projet>

    @POST("projet/user")
    suspend fun getAllProjectsById(@Query("userId") userId: String): List<Projet>


    @GET("projet/user")
    suspend fun getAllProjectsByIdd(@Query("id") userId: String): List<Projet>

    @GET("projet/{id}")
    suspend fun getProjectById(@Path("id") id: String): Projet

    @PATCH("projet/update")
    suspend fun updateProject(@Body projet: Projet): Projet

    @DELETE("projet/delete")
    suspend fun deleteProject(@Query("id") id: String): Projet

    @POST("/project-filter/filterPer")
    suspend fun getOngoingProjects(@Query("userId") userId: String): List<Project>

}
