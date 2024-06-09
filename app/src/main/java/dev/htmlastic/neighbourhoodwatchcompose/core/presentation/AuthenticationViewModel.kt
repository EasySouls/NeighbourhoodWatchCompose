package dev.htmlastic.neighbourhoodwatchcompose.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.htmlastic.neighbourhoodwatchcompose.core.data.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(): ViewModel() {

    private val _authenticated = MutableStateFlow(false)
    val authenticated = _authenticated.asStateFlow()

    fun signInWithGoogle(
        tokenId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.google(
                            token = tokenId,
                            type = GoogleAuthType.ID_TOKEN
                        )
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(500)
                        _authenticated.value = true
                    } else {
                        onFailure(Exception("Sign in failed"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }
}