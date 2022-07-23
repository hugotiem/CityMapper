package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.hugotiem.citymapper.model.User
import fr.hugotiem.citymapper.services.AuthService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AuthViewModel: ViewModel() {

    private val authService: AuthService = AuthService()

    val userLiveData: MutableLiveData<User> = MutableLiveData<User>()

    suspend fun signIn(email: String?, name: String?): Unit = coroutineScope {
        var task = async {}
        task.await()
    }

    fun isConnected(): Boolean = authService.isConnected()
}