package app.culturedev.cultureconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import app.culturedev.cultureconnect.data.repository.CafeRepo

class LoginViewModel(private val repository:CafeRepo): ViewModel() {
    fun handleLogin(email: String, password: String) = repository.handleLogin(email, password)
}