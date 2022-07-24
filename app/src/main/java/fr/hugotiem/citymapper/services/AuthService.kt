package fr.hugotiem.citymapper.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance();

    fun getInstance(): FirebaseAuth {
        return auth
    }

    fun init(navController: NavController): Unit {
        val authStateListener =
            AuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser == null) {
                    navController.navigate("login")
                } else {
                    navController.navigate("home")
                }
            }
        auth.addAuthStateListener(authStateListener);
    }

    fun isConnected(): Boolean {
        var currentUser = auth.currentUser
        return currentUser != null;
    }

    fun getFirebaseUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout(): Unit {
        auth.signOut();
    }
}


class AuthResultContract : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return getGoogleSignInClient(context).signInIntent.putExtra("input", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//         Request id token if you intend to verify google user from your backend server
//        .requestIdToken(context.getString(R.string.backend_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }
}