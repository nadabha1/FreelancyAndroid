package tn.esprit.freelancy.model.projet

data class ApplicationResponse(
    val _id: String,
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
data class ApplicationStatusResponse(
    val freelancer: String,
    val status: String
)
data class ApplicationStatusRequest(
    val freelancerId: String,
    val projectId: String
)
data class ApplicationStatusResponsem(
    val statusMap: Map<String, String> // Assurez-vous que le champ est nommé 'statusMap'
)
