//package tn.esprit.freelancy.view.chat
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import tn.esprit.freelancy.view.Icon
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatScreen() {
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /* Add your action here */ },
//                backgroundColor = MaterialTheme.colorScheme.primary
//            ) {
//                androidx.compose.material3.Icon(
//                    imageVector = Icons.Default.Message, // Replace with the icon of your choice
//                    contentDescription = "New Chat",
//                    tint = Color.White
//                )
//            }
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFF001F3F)) // Adjust the color to match your background
//                .padding(it)
//        ) {
//            // Top Section
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Profile Icon
//                IconButton(onClick = { /* Profile click action */ }) {
//                    Icon(
//                        imageVector = Icons.Default.Person, // Replace with your avatar icon
//                        contentDescription = "Profile",
//                        tint = Color.White
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//
//                // Username
//                Text(
//                    text = "thunder buddy",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.White,
//                    modifier = Modifier.weight(1f)
//                )
//
//                // Search Icon
//                IconButton(onClick = { /* Search click action */ }) {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search",
//                        tint = Color.White
//                    )
//                }
//
//                // Menu Icon
//                IconButton(onClick = { /* Menu click action */ }) {
//                    Icon(
//                        imageVector = Icons.Default.MoreVert,
//                        contentDescription = "Menu",
//                        tint = Color.White
//                    )
//                }
//            }
//
//            // Chats Section
//            Text(
//                text = "Chats",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color.White,
//                modifier = Modifier.padding(horizontal = 16.dp)
//            )
//        }
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun ChatScreenPreview() {
//    ChatScreen()
//}
