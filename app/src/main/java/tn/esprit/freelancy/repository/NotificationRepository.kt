package tn.esprit.freelancy.repository

// NotificationRepository.kt
import tn.esprit.freelancy.model.projet.Notification
import tn.esprit.freelancy.remote.projet.ProjetAPI

class NotificationRepository(private val apiService: ProjetAPI) {

    // Fetch notifications for the entrepreneur
    suspend fun getNotifications(entrepreneurId: String): List<Notification> {
        return apiService.getNotifications(entrepreneurId)
    }

    suspend fun markAsRead(notificationId: String) {
        apiService.markNotificationAsRead(notificationId)
    }
}
