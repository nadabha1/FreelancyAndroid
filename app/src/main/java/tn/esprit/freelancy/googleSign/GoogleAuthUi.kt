//package tn.esprit.freelancy.googleSign
//
//import android.content.Context
//import android.content.Intent
//import android.content.IntentSender
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.firebase.Firebase
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.auth
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.tasks.await
//import tn.esprit.freelancy.R
//import tn.esprit.freelancy.model.chat.SignInResult
//import tn.esprit.freelancy.model.chat.UserData
//import tn.esprit.freelancy.viewModel.chat.ChatViewModel
//
//class GoogleAuthUi(    private val context: Context,
//    private val oneTapClient: SignInClient,
//    val viewModel: ChatViewModel
//) {
//    private val auth = Firebase.auth
//
//    suspend fun signIn():IntentSender?{
//        val result =try {
//            oneTapClient.beginSignIn(
//                buildSignInRequest()
//            ).await()
//        }catch (e: Exception)
//        {
//            e.printStackTrace()
//            if (e is  CancellationException) throw e
//               null
//
//        }
//        return result?.pendingIntent?.intentSender
//    }
//    private  fun buildSignInRequest():BeginSignInRequest{
//        return  BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
//            GoogleIdTokenRequestOptions.builder().setSupported(true)
//                .setFilterByAuthorizedAccounts(false).setServerClientId("487878832428-lpkn1qh554332dpp3ils9prbsmgpuirf.apps.googleusercontent.com").build()
//        ).setAutoSelectEnabled(true).build()
//    }
//
//
//    suspend fun signInWithIntent(intent: Intent): SignInResult {
//        viewModel.resetState()
//        val cred= oneTapClient.getSignInCredentialFromIntent(intent)
//        val googleIdToken =cred.googleIdToken
//        val googleCred = GoogleAuthProvider.getCredential(googleIdToken,null)
//        return try {
//            val user = auth.signInWithCredential(googleCred).await().user
//            SignInResult(
//                errorMessage = null,
//                data = user?.run {
//                    UserData(
//                        email = email.toString(),
//                        userId = uid,
//                        username = displayName.toString(),
//                        profilePictureUrl = photoUrl.toString().substring(0,photoUrl.toString().length)
//                    )
//                }
//            )
//        }catch (e: Exception){
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            SignInResult(
//                errorMessage = e.message,
//                data = null)
//        }
//    }
//}