package tn.esprit.freelancy.viewModel.chat

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tn.esprit.freelancy.googleSign.CHAT_COLLECTION
import tn.esprit.freelancy.googleSign.USERS_COLLECTION
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.ChatData
import tn.esprit.freelancy.model.chat.ChatUserData
import tn.esprit.freelancy.model.chat.Message
import tn.esprit.freelancy.model.chat.SignInResult
import tn.esprit.freelancy.model.chat.SignInResult2
import tn.esprit.freelancy.model.user.GetUserIdResponse
import tn.esprit.freelancy.model.user.SignupRequest
import tn.esprit.freelancy.model.user.SignupResponse
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.model.user.UserProfileFireBase
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.HomeViewModelFactory

class ChatViewModel(private val sessionManager: SessionManager) : ViewModel()
{
    var reply by mutableStateOf("")
    private val _tp = MutableStateFlow<ChatData?>(null) // État réactif mutable
    var tp: StateFlow<ChatData?> = _tp.asStateFlow()    // État exposé en lecture seule

    private val _state=MutableStateFlow(AppState())
    val state=_state.asStateFlow()

    private val userCollection = Firebase.firestore.collection(USERS_COLLECTION)
    var userChatListener : ListenerRegistration?=null
     // Use the factory
     private val _chats = MutableStateFlow<List<ChatData>>(emptyList())
    val chats = _chats.asStateFlow()
    var tpListenner :ListenerRegistration?=null
    fun resetState(){
    }
    fun hideDialog()
    {
        _state.update {
            it.copy(
                showDialog = false
            )
        }
    }
    fun showChatDialog() {
        _state.update {
            it.copy(
                showDialog = true
            )
        }
    }


    fun setSrEmail(email: String) {
        _state.update {
            it.copy(
                srEmail = email
            )
        }
    }


fun showChats(userId: String) {
    println("Récupération des chats pour l'utilisateur : $userId")

    userChatListener = Firebase.firestore.collection(CHAT_COLLECTION)
        .where(
            Filter.or(
                Filter.equalTo("user1.userId", userId),
                Filter.equalTo("user2.userId", userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                println("Erreur lors de la récupération des chats : $error")
                return@addSnapshotListener
            }

            if (value != null) {
                println("Nombre de chats récupérés : ${value.documents.size}")
                _chats.value = value.documents.mapNotNull { document ->
                    val chat = document.toObject(ChatData::class.java)
                    if (chat != null && chat.last != null) {
                        chat.last.content = chat.last.content.ifEmpty { "No message" }
                    }
                    chat
                }.sortedByDescending { it.last?.time }

            } else {
                println("Aucun chat trouvé pour cet utilisateur.")
            }
        }
}



    fun addChat(srEmail: String) {
        val email = sessionManager.getSession()?.email
        if (email != null) {
            fetchUserProfile(email)
            println("chat email: $email")
            println(_userProfile2.value)
        }
        println("srEmail: $email")
        println(srEmail)
        userCollection.whereEqualTo("email", srEmail).get().addOnSuccessListener {
            if (it.isEmpty) {
                println("failed to find email firebase")
            } else {
                val chatPartner = it.toObjects(UserProfileFireBase::class.java).firstOrNull()
                println("chatPartner: $chatPartner")

                val id = Firebase.firestore.collection(CHAT_COLLECTION).document().id
                val chat = ChatData(
                    chat_id = id,
                    last = Message(
                        content = "hii",
                        senderId = state.value.userProfil?.userId ?: "system",
                        time = Timestamp.now(),),
                    user1 = ChatUserData(
                        userId = _userProfile2.value?.idUser ?: "",
                        username = _userProfile2.value?.username ?: "",
                        email = _userProfile2.value?.email ?: ""
                    ),
                    user2 = ChatUserData(
                        userId = chatPartner?.userId ?: "",
                        username = chatPartner?.username ?: "",
                        email = chatPartner?.email ?: ""
                    ),

                    )

                // Log avant d'ajouter dans Firestore
                println("Chat à ajouter : $chat")

                Firebase.firestore.collection(CHAT_COLLECTION).document(id).set(chat)
                    .addOnSuccessListener { println("Chat ajouté avec succès") }
                    .addOnFailureListener { e -> println("Erreur : $e") }
            }
        }
    }

//    fun getTp(chatId:String){
//        if (chatId.isEmpty()) {
//            println("Erreur : chatId est vide dans getTp")
//            return
//        }
//        println("Appel de getTp avec chatId : $chatId")
//
//
//
//        tpListenner?.remove()
//        tpListenner = Firebase.firestore.collection(CHAT_COLLECTION).document(chatId)
//            .addSnapshotListener { snp, err ->
//                if (err != null) {
//                    Log.e("ChatViewModel", "Erreur lors de l'écoute des données : $err")
//                    return@addSnapshotListener
//                }
//                if (snp != null && snp.exists()) {
//                    val chatData = snp.toObject(ChatData::class.java)
//                    if (chatData != null) {
//                        tp = chatData // Assurez-vous que `tp` est de type mutableStateOf
//                        println("tp mis à jour : $tp")
//                    }
//                    _tp.value = snp.toObject(ChatData::class.java)
//                    println("tp mis à jour : $tp")
//
//                } else {
//                    Log.w("ChatViewModel", "Document introuvable")
//                    tp = ChatData()
//                    println("tp: $tp")
//
//                }
//            }
//
//    }

    fun getTp(chatId: String) {
        if (chatId.isEmpty()) {
            println("Erreur : chatId est vide dans getTp")
            return
        }
        println("Appel de getTp avec chatId : $chatId")

        // Supprimez l'écouteur précédent pour éviter les fuites de mémoire
        tpListenner?.remove()

        tpListenner = Firebase.firestore.collection(CHAT_COLLECTION).document(chatId)
            .addSnapshotListener { snp, err ->
                if (err != null) {
                    Log.e("ChatViewModel", "Erreur lors de l'écoute des données : $err")
                    return@addSnapshotListener
                }

                if (snp != null && snp.exists()) {
                    val chatData = snp.toObject(ChatData::class.java)
                    if (chatData != null) {
                        _tp.value = chatData // Met à jour l'état réactif
                        println("tp mis à jour : ${_tp.value}")
                    }
                } else {
                    Log.w("ChatViewModel", "Document introuvable")
                    _tp.value = ChatData() // Réinitialisation en cas d'absence de données
                    println("tp réinitialisé : ${_tp.value}")
                }
            }
    }


    fun getMessages(chatId: String) {
        tpListenner?.remove()
        tpListenner = Firebase.firestore.collection(CHAT_COLLECTION).document(chatId)
            .addSnapshotListener { snp, err ->
                if (err != null) {
                    Log.e("ChatViewModel", "Erreur lors de l'écoute des données : $err")
                    return@addSnapshotListener
                }
                if (snp != null && snp.exists()) {
                    tp = (snp.toObject(ChatData::class.java) ?: ChatData()) as StateFlow<ChatData?>
                    println("Conversation mise à jour : $tp")
                } else {
                    Log.w("ChatViewModel", "Document introuvable pour chatId=$chatId")
                }
            }
    }


    fun setChatUser(usr: ChatUserData, id: String) {
        _state.update {
            it.copy(
                User2 = usr,
                chat_id = id
            )
        }
    }

    fun fetchChatUserData(userId: String, onResult: (ChatUserData?) -> Unit) {
        Firebase.firestore.collection(USERS_COLLECTION)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val userData = documents.documents.firstOrNull()?.toObject(ChatUserData::class.java)
                if (userData != null) {
                    println("Données utilisateur récupérées : $userData")
                    onResult(userData)
                } else {
                    println("Aucun utilisateur trouvé pour userId=$userId")
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                println("Erreur lors de la récupération des données utilisateur : ${exception.message}")
                onResult(null)
            }
    }

    private val _userProfile2 = MutableStateFlow<UserProfileComplet?>(null)
    val userProfile2: StateFlow<UserProfileComplet?> = _userProfile2
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun fetchUserProfile(email: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                println("Fetching user profile for email: $email") // Debug log
                val user = RetrofitClient.authService.getUserProfile(email)
                println("Chatuserfetch: $user") // Debug log
                _userProfile2.value = user // Assign the user to StateFlow
                val roleName = RetrofitClient.authService.getRoleName(
                    GetUserIdResponse(user.
                idUser)
                )
            } catch (e: Exception) {
                println("Error fetching user profile: ${e.message}") // Debug log
                _userProfile2.value = null

            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun onMessageTextChanged(newText: String) {
        _state.update { currentState ->
            currentState.copy(messageText = newText)
        }
    }
    // Fonction pour envoyer un message
    fun sendMessage(chatId: String) {
        if (reply.isBlank()) return // Ne rien envoyer si le message est vide

        val senderId = sessionManager.getSession()?.id ?: "unknown"

        val newMessage = Message(
            msgId = "", // Générer un ID unique (Firestore le fait automatiquement si non fourni)
            senderId = senderId,
            content = reply,
            time = Timestamp.now(),
            read = false
        )

        // Ajouter le message à la collection Firestore
        Firebase.firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(newMessage)
            .addOnSuccessListener {
                println("Message envoyé : ${newMessage.content}")
                reply = "" // Réinitialiser le champ de texte
            }
            .addOnFailureListener { e ->
                println("Erreur lors de l'envoi du message : ${e.message}")
            }
    }
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun fetchMessages(chatId: String) {
        Firebase.firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("time") // Trier les messages par ordre chronologique
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Erreur lors de la récupération des messages : ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val fetchedMessages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                    _messages.value = fetchedMessages
                }
            }
    }


}