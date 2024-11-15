package tn.esprit.freelancy.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tn.esprit.freelancy.viewModel.ForgotPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel) {
    val currentStep by viewModel.currentStep.collectAsState()

    when (currentStep) {
        1 -> ForgotPasswordStep(viewModel)
        2 -> VerifyOtpStep(viewModel)
        3 -> ResetPasswordStep(viewModel)

    }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    errorMessage?.let {
        Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
    }

    successMessage?.let {
        Text(text = it, color = Color.Green, modifier = Modifier.padding(16.dp))
    }
}
