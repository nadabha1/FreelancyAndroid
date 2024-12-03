package tn.esprit.freelancy.model.projet

data class ApplicationResponse(
    val id: String,
    val freelancer: String,
    val project: String,
    val status: String
)
// ApplicationRequest.kt
data class ApplicationRequest(
    val freelancer: String,  // The freelancer's ID
    val project: String ,
    val status: String
)
