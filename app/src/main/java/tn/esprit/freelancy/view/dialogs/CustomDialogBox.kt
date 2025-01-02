package tn.esprit.freelancy.view.dialogs

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tn.esprit.freelancy.model.chat.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialogBox(
    state: AppState,
    setEmail: (String) -> Unit = {},
    hideDialog: () -> Unit = {},
    addChat: () -> Unit = {},
) {

    Dialog(
        onDismissRequest = { hideDialog() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(5.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxSize(.90f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {

                Text(
                    text = "Enter Email Id",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                state.srEmail?.let {
                    OutlinedTextField(
                        label = { Text(text = "Email") },
                        value = it,
                        onValueChange = {
                            setEmail(it)
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = hideDialog) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    TextButton(onClick = addChat) {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogBoxPreview() {
    val mockState = remember { AppState(srEmail = "") }
    CustomDialogBox(
        state = mockState,
        setEmail = { mockState.srEmail = it },
        hideDialog = { /* Preview: Dialog dismissed */ },
        addChat = { /* Preview: Chat added */ }
    )
}

@Composable
fun Navbar() {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Messages", Icons.Default.MailOutline, "messages"),
        BottomNavItem("Profile", Icons.Default.Person, "profile"),
        BottomNavItem("Alerts", Icons.Default.Notifications, "alerts")
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
                selected = false, // Adjust based on navigation state
                onClick = { /* Handle navigation */ }
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

@Preview(showBackground = true)
@Composable
fun NavbarPreview() {
    Navbar()
}
