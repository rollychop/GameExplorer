package com.passive.gameexplorer.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passive.gameexplorer.GameIdListActivity
import com.passive.gameexplorer.model.GameDetailModel
import com.passive.gameexplorer.model.GameModel
import com.passive.gameexplorer.model.UserProfile
import com.passive.gameexplorer.repository.DeviceIdRepository
import com.passive.gameexplorer.repository.GameRepository
import com.passive.gameexplorer.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val deviceIdRepository: DeviceIdRepository,
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val gameType = savedStateHandle.get<String>(GameIdListActivity.GAME_TYPE_KEY) ?: "BGMI"


    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile
        .onStart { fetchProfile() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), null)


    private val _gameDetails = MutableStateFlow<GameDetailModel?>(null)
    val gameDetails: StateFlow<GameDetailModel?> = _gameDetails

    // State to store game IDs fetched from Firestore
    private val _gameIds = MutableStateFlow<List<GameModel>>(emptyList())
    val gameIds: StateFlow<List<GameModel>> = _gameIds
        .onStart { fetchGameIds(gameType) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())


    // State to handle loading indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State to handle error message
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    // State to handle error message
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    val deviceId by lazy { deviceIdRepository.getDeviceId() }

    // Fetch game IDs based on the selected game type (BGMI or Free Fire)
    private fun fetchGameIds(gameType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            try {
                // Simulate repository call to fetch game IDs
                val result = gameRepository.getGameIds(gameType)

                result.onSuccess { gameIdList ->
                    _gameIds.value = gameIdList
                }
                result.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Error fetching game IDs"
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchGameDetails(gameType: String, gameId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _gameDetails.value = null
            _errorMessage.value = ""
            gameRepository.getGameDetails(
                deviceId = deviceId,
                gameId = gameId,
                gameType = gameType
            ).fold(
                onSuccess = {
                    _gameDetails.value = it
                    _isLoading.value = false
                },
                onFailure = {
                    _isLoading.value = false
                    _errorMessage.value = it.message ?: "Unknown error occurred"
                }
            )
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            profileRepository.getProfile(deviceId).fold(
                onSuccess = {
                    _profile.value = it
                },
                onFailure = {
                    _errorMessage.value = it.message ?: "Unknown error occurred"
                })
        }
    }

    // Rate a game ID
    fun rateGame(
        gameType: String,
        gameId: String,
        rating: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = gameRepository.rateGameId(
                    deviceId = deviceId,
                    gameId = gameId,
                    gameType = gameType,
                    rating = rating
                )

                result.onSuccess {
                    _message.value = "Rating added and 10 coins rewarded"
                    fetchGameDetails(gameType, gameId)
                }
                result.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to add rating"
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Comment on a game ID
    fun commentOnGame(gameType: String, gameId: String, comment: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = gameRepository.commentOnGameId(deviceId, gameId, gameType, comment)

                result.onSuccess {
                    _message.value = "Comment posted and 10 coins deducted"
                }
                result.onFailure { exception ->
                    _errorMessage.value = exception.message ?: "Failed to post comment"
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = ""
        _message.value = ""
    }
}
