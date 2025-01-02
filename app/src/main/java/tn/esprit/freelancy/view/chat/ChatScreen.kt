package tn.esprit.freelancy.view.chat

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import tn.esprit.freelancy.R
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.ChatData
import tn.esprit.freelancy.model.chat.ChatUserData
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.view.Icon
import tn.esprit.freelancy.view.dialogs.CustomDialogBox
import tn.esprit.freelancy.viewModel.chat.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel,showSingleChat: (ChatUserData,String) -> Unit,navController: NavController) {
    val state by viewModel.state.collectAsState()
    val pandding by animateDpAsState(targetValue = 10.dp, label = "")
    val chats by viewModel.chats.collectAsState()
    val userId = SessionManager(context = LocalContext.current).getUserId()
    val filterChats = chats
    val selectedItem = remember {
        mutableStateListOf<String>()
    }
        println(userId)
        viewModel.showChats(userId.toString())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showChatDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "New Chat",
                    tint = Color.White
                )
            }
        }
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF001F3F))
                .padding(innerPadding)
        ) {
            // Contenu principal
            Text(
                text = "Chats",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            Column(
                modifier = Modifier.padding(top = 30.dp)
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = pandding).background(
                            color = colorScheme.background.copy(
                                alpha = 0.2f

                            ),
                            shape = RoundedCornerShape(30.dp,30.dp)
                        ).border(
                            0.05.dp,
                            color = Color(0xFF001F3F),
                            shape = RoundedCornerShape(30.dp,30.dp)
                        )

                )
                {
                    item {
                        Text(text = "Chats",
                            modifier = Modifier.padding(16.dp,16.dp,16.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        )
                    }
                    items(filterChats) { chat ->
                        println("Affichage du chat : ${chat.chat_id}, dernier message : ${chat.last?.content}")
                        val chatUser = if (chat.user1.userId != userId) chat.user1 else chat.user2

                        ChatItem(
                            state = state,
                            userData = chatUser,
                            chat = chat,
                            mode = false,
                            isSelected = selectedItem.contains(chat.chat_id),
                            showSingleChat = { userData, chatId ->
                                if (userData != null && !chatId.isNullOrEmpty()) {
                                    println("Navigation vers ChatMess avec chatId=$chatId et userId=${userData.userId}")
                                    navController.navigate("ChatMess/$chatId/${userData.userId}")
                                } else {
                                    println("Erreur : Données nulles lors de la navigation")
                                }
                            }

                        )

                    }
                }
            }


        }

        // Affichage conditionnel du dialogue
        if (state.showDialog) {
            CustomDialogBox(
                state = state,
                hideDialog = { viewModel.hideDialog() },
                addChat = {
                    viewModel.addChat(state.srEmail)
                    viewModel.hideDialog()
                },
                setEmail = { email -> viewModel.setSrEmail(email) }
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(viewModel = ChatViewModel(sessionManager = SessionManager(context = LocalContext.current)), showSingleChat = { _, _ -> }, navController = rememberNavController())
}
@Composable
fun ChatItem(state: AppState,
             userData :ChatUserData,
             chat: ChatData,
             mode :Boolean,
             isSelected:Boolean,
             showSingleChat: (ChatUserData, String) -> Unit
) {
    val formatter = remember {
        SimpleDateFormat(("hh:mm a"), Locale.getDefault()
        )
    }
    val userId = SessionManager(context = LocalContext.current).getUserId()

    val  color = if (!isSelected) {Color.Transparent} else
    {
        colorScheme.onPrimary
    }
    Row(
        modifier = Modifier.background(
            color = color
        ).fillMaxWidth().padding(16.dp,12.dp).clickable{
            showSingleChat(
                userData,chat.chat_id
            )
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("")
                .crossfade(true)
                .allowHardware(false).build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(70.dp).clip(CircleShape)
        )

        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(.95f)

            ) {
                Text(text = if(userData.userId==userId ){
                    userData.username.orEmpty()+"(You)"
                }
                    else{
                        userData.username.orEmpty()
                    },
                    modifier =Modifier.width(150.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = if (chat.last?.time!=null)
                {
                    formatter.format(chat.last.time.toDate())
                }
                    else "",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Light
                    ),
                    color = Color.Gray


                )
            }

            AnimatedVisibility(chat.last?.time!=null && userData.typing) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(chat.last?.senderId == userId){
                        Icon(
                            icon = Icons.Filled.CheckCircle,
                            contentDescription = "",
                            modifier = Modifier.padding(end = 5.dp).size(10.dp),
                            tint = if (chat.last?.read?:false)
                                Color(0xFF00FF00) else Color.Gray
                        )
                    }

                    }
                }
            }
        }
    }


