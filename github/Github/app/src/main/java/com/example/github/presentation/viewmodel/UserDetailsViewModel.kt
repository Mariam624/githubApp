package com.example.github.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.datasource.ApiResponse
import com.example.github.domain.Repository
import com.example.github.presentation.data.User
import kotlinx.coroutines.launch

class UserDetailsViewModel : ViewModel() {

    private val repository = Repository()

    private val _userLiveData = MutableLiveData<ApiResponse<User>>()
    val userLiveData: LiveData<ApiResponse<User>> = _userLiveData

    fun getUser(name: String) {
        viewModelScope.launch {
            _userLiveData.value = repository.getUser(name)
        }
    }
}