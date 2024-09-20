package com.example.github.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.datasource.ApiResponse
import com.example.github.domain.Repository
import com.example.github.presentation.data.User
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private val repository = Repository()

    private val _userListLiveData = MutableLiveData<ApiResponse<List<User>>>()
    val userListLiveData: LiveData<ApiResponse<List<User>>> = _userListLiveData

    fun getUserList() {
        viewModelScope.launch {
            _userListLiveData.value = repository.getUsers()
        }
    }
}