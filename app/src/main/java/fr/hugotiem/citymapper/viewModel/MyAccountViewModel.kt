package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import fr.hugotiem.citymapper.services.AuthService
import kotlinx.coroutines.coroutineScope

class MyAccountViewModel: ViewModel() {

    private val authService: AuthService = AuthService()

    fun getUsername(): String? {
        return authService.getFirebaseUser()?.displayName
    }

    fun logout(): Unit {
        authService.logout()
    }
}