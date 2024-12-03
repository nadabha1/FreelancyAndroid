package tn.esprit.freelancy.viewModel.projet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tn.esprit.freelancy.model.projet.Notification
import tn.esprit.freelancy.repository.NotificationRepository
import tn.esprit.freelancy.session.SessionManager

class NotificationViewModel(private val notificationRepository: NotificationRepository,sessionManager: SessionManager) : ViewModel() {

    var notifications = mutableStateOf<List<Notification>>(emptyList())
    val userId: String? = sessionManager.getUserId()

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val entrepreneurId = userId ?: ""
                println("Fetching notifications for entrepreneurId: $entrepreneurId")
                val fetchedNotifications = notificationRepository.getNotifications(entrepreneurId)
                notifications.value = fetchedNotifications
                println("Fetched notifications: $fetchedNotifications")
            } catch (e: Exception) {
                // Handle error, show message etc.
            }
        }



    }


    fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            // Implement API call to mark the notification as read
            notificationRepository.markAsRead(notificationId)
        }
    }
}
