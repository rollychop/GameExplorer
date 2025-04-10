package com.passive.gameexplorer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passive.gameexplorer.model.ProfileField
import com.passive.gameexplorer.model.UserProfile
import com.passive.gameexplorer.repository.DeviceIdRepository
import com.passive.gameexplorer.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val deviceIdRepository: DeviceIdRepository
) : ViewModel() {



    private val _profileState = MutableStateFlow<UserProfile?>(null)
    val profileState: StateFlow<UserProfile?> = _profileState

    private val deviceId: String by lazy { deviceIdRepository.getDeviceId() }


    init {
        fetchProfile()
    }
    fun saveField(
        field: ProfileField, value: String,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = profileRepository.updateField(deviceId, field.key, value)
            result.onSuccess {
                fetchProfile()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    onFailure()
                }
            }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            val result = profileRepository.getProfile(deviceId)
            result.onSuccess { profile ->
                _profileState.value = profile
            }.onFailure {
                _profileState.value = null
            }
        }
    }
}
