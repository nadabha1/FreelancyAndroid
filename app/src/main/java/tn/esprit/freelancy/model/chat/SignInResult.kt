package tn.esprit.freelancy.model.chat

import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import tn.esprit.freelancy.model.user.UserProfileFireBase
data class SignInResult2(
    val data:UserProfileFireBase?,
    val errorMessage:String?

)

data class SignInResult(
    val data:UserData?,
    val errorMessage:String?

)
data class UserData(
    val userId:String,
    val username:String,
    val profilePictureUrl:String,
    val email:String
)


data class UserData2(
    val userId:String,
    val username:String,
    val profilePictureUrl:String,
    val email:String,
    val role:String,
    val password:String
)
data class AppState(
    val isSignedIn:Boolean=false,
    val userData: UserData?=null,
    var srEmail:String="",
    val showDialog:Boolean=false,
    val userProfil : UserProfileFireBase?=null,
    val User2: ChatUserData?=null,
    val chat_id: String="",
    val messageText: String = ""

    )
data class ChatData(
    val chat_id: String = "",
    val last: Message? = null,
    val user1: ChatUserData = ChatUserData(),
    val user2: ChatUserData = ChatUserData()
)
data class Message(
    val  msgId: String="",
    val senderId : String="",
    val repliedMessage :String="",
    val reaction : List<Reaction> = emptyList(),
    val imageUrl : String="",
    val fireUrl : String="",
    val fileName : String="",
    val filesize : String="",
    val vidUrl : String="",
    val progres :String="",
    var content : String="",
    val read : Boolean=false,
    val time : Timestamp ? =null,
    val forwarded : Boolean=false
)

data class  Reaction(
    val  ppurl : String="",
    val username: String="",
    val userId: String="",
    val reaction: String="",
)

data class ChatUserData(
    val userId: String="",
    val typing: Boolean=false,
    val bio : String="",
    val username: String = "Utilisateur inconnu",
    val ppurl: String = "https://via.placeholder.com/150",

    val email: String="",
    val status:Boolean=false,
    val unread : Int=0
    )
