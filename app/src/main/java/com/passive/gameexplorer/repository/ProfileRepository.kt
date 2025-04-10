package com.passive.gameexplorer.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.passive.gameexplorer.mapper.UserProfileMapper
import com.passive.gameexplorer.model.UserProfile
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    companion object {
        private const val TAG = "ProfileRepository"
    }

    // Update a specific field in the user's profile
    suspend fun updateField(deviceId: String, field: String, value: String): Result<Unit> {
        return try {
            val fieldMap = mapOf(field to value)
            firestore.collection("profiles")
                .document(deviceId)
                .update(fieldMap)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "updateField: Error updating field $field with value $value for device $deviceId",
                e
            )
            Result.failure(e)
        }
    }

    // Get the profile or create it if it doesn't exist
    suspend fun getProfile(deviceId: String): Result<UserProfile> {
        return try {
            val snapshot = firestore.collection("profiles").document(deviceId).get().await()

            if (snapshot.exists()) {
                // Profile exists, map it to UserProfile
                Result.success(UserProfileMapper.fromDocumentSnapshot(snapshot))
            } else {
                // Profile doesn't exist, create a new one
                val newProfile = createNewProfile(deviceId)
                Result.success(newProfile)
            }
        } catch (e: Exception) {
            if (e is FirebaseFirestoreException) {
                if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                    createNewProfile(deviceId)
                }
            }
            Log.e(TAG, "Error fetching profile for deviceId $deviceId", e)
            Result.failure(e)
        }
    }

    // Create a new profile if it doesn't exist
    private suspend fun createNewProfile(deviceId: String): UserProfile {
        // Create a default profile with initial data
        val newProfileData = UserProfile(
            deviceId = deviceId,
            language = "English",  // default language
            gameLevel = "0-25",    // default game level
            favoriteGame = "PUBG", // default favorite game
            topPlayer = "Player1", // default top player
            createdAt = System.currentTimeMillis()  // timestamp for profile creation
        )

        // Save the new profile to Firestore
        firestore.collection("profiles")
            .document(deviceId)
            .set(newProfileData)
            .await()

        // Return the new profile
        return newProfileData
    }
}
