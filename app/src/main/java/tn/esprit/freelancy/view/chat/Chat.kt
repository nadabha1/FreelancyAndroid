package tn.esprit.freelancy.view.chat

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import tn.esprit.freelancy.R
import tn.esprit.freelancy.model.chat.AppState
import tn.esprit.freelancy.model.chat.ChatUserData
import tn.esprit.freelancy.model.chat.Message
import tn.esprit.freelancy.model.user.UserProfileComplet
import tn.esprit.freelancy.session.SessionManager
import tn.esprit.freelancy.view.Icon
import tn.esprit.freelancy.viewModel.HomeViewModel
import tn.esprit.freelancy.viewModel.chat.ChatViewModel
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Chat(
    viewModel: ChatViewModel = ChatViewModel(sessionManager = SessionManager(context = LocalContext.current)),
    userProfile: UserProfileComplet,
    userData: ChatUserData = ChatUserData(),
    chatId: String = "",
    state: AppState = AppState(),
    onBack: () -> Unit = {},
    context: Context = LocalContext.current,
    sessionManager: SessionManager = SessionManager(context),
    navController: NavController = rememberNavController()
) {
    val tp by viewModel.tp.collectAsState(initial = null)
    val currentState by viewModel.state.collectAsState()
    val messages by viewModel.messages.collectAsState() // Observing messages dynamically
    val focusRequester = remember { FocusRequester() }

    // Fetch messages when the chat is opened
    LaunchedEffect(chatId) {
        viewModel.fetchMessages(chatId)
    }

    // Handle empty user data or chat ID
    if (userData.username.isEmpty() || chatId.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chargement des données...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        AsyncImage(
                            model = userData.ppurl.ifEmpty { "https://via.placeholder.com/150" },
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(40.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = userData.username,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Retour",
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

            )
        },
        bottomBar = {
            // Message Input Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(32.dp)
                    )
            ) {
                // Message Input Field
                TextField(
                    value = viewModel.reply,
                    onValueChange = { viewModel.reply = it },
                    placeholder = { Text("Écrire un message...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
                // Send Button
                androidx.compose.material3.IconButton(onClick = {
                    viewModel.sendMessage(chatId) // Send the message
                }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "Envoyer",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    ) { innerPadding ->
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.whatsapp),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f) // Semi-transparent background
        )

        // Message List
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(message = msg, isCurrentUser = msg.senderId == userProfile.idUser)
            }
        }
    }
}
@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(
                    if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message.time?.toDate().toString(), // Format the timestamp as needed
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
