package fr.hugotiem.citymapper.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import fr.hugotiem.citymapper.model.User
import fr.hugotiem.citymapper.services.AuthService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AuthViewModel: ViewModel() {

    private val authService: AuthService = AuthService()

    val userLiveData: MutableLiveData<User> = MutableLiveData<User>()

    fun getAuthInstance(): FirebaseAuth {
        return authService.getInstance()
    }

    fun firebaseAuthInit(navController: NavController) {
        authService.init(navController)
    }

    fun isConnected(): Boolean = authService.isConnected()
}