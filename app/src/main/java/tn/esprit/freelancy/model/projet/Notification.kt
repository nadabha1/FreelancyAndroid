package tn.esprit.freelancy.model.projet


data class Notification(
    val _id: String,
    val projectId: String,
    val entrepreneurId: String,
    val message: String,
    val status: String // 'unread' or 'read'
)