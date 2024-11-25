package tn.esprit.freelancy.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tn.esprit.freelancy.repository.CvRepository
import tn.esprit.freelancy.remote.RetrofitClient
import tn.esprit.freelancy.viewModel.CvViewModel
import tn.esprit.freelancy.viewModel.CvViewModelFactory
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvAnalysisScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val repository = CvRepository(RetrofitClient.cvApiService)
    val factory = CvViewModelFactory(repository)
    val viewModel: CvViewModel = viewModel(factory = factory)

    val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
    val analysisResult by viewModel.analysisResult.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        selectedFileUri.value = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CV Analysis") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Upload and Analyze Your CV")
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                filePickerLauncher.launch("application/pdf") // Only PDFs
            }) {
                Text("Select CV")
            }

            Spacer(modifier = Modifier.height(20.dp))

            selectedFileUri.value?.let { uri ->
                Button(onClick = {
                    val fileName = "cv.pdf" // Replace with dynamic name if needed
                    viewModel.uploadCv(context, uri, fileName)
                }) {
                    Text("Analyze CV")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            analysisResult?.let { entities ->
                entities.forEach { entity ->
                    Text(text = "${entity.entity_group}: ${entity.word} (Score: ${entity.score})")
                }
            }


            errorMessage?.let { error ->
                Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
