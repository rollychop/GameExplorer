package com.passive.gameexplorer.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.passive.gameexplorer.model.ProfileFieldNames
import com.passive.gameexplorer.model.UserProfile

object UserProfileMapper {

    // Convert Firestore document to UserProfile
    fun fromDocumentSnapshot(snapshot: DocumentSnapshot): UserProfile {
        return UserProfile(
            deviceId = snapshot.getString(ProfileFieldNames.DEVICE_ID) ?: "",
            language = snapshot.getString(ProfileFieldNames.LANGUAGE),
            gameLevel = snapshot.getString(ProfileFieldNames.GAME_LEVEL) ?: "",
            favoriteGame = snapshot.getString(ProfileFieldNames.FAVORITE_GAME) ?: "",
            topPlayer = snapshot.getString(ProfileFieldNames.TOP_PLAYER) ?: "",
            createdAt = snapshot.getDate(ProfileFieldNames.CREATED_AT)?.time ?: 0,
            coins = (snapshot.getLong(ProfileFieldNames.COINS) ?: 0L).toInt()
        )
    }

    // Convert UserProfile to Firestore document map
    fun toMap(userProfile: UserProfile): Map<String, Any> {
        return mapOf(
            ProfileFieldNames.DEVICE_ID to userProfile.deviceId,
            ProfileFieldNames.LANGUAGE to (userProfile.language ?: ""),
            ProfileFieldNames.GAME_LEVEL to (userProfile.gameLevel ?: ""),
            ProfileFieldNames.FAVORITE_GAME to (userProfile.favoriteGame ?: ""),
            ProfileFieldNames.TOP_PLAYER to (userProfile.topPlayer ?: ""),
            ProfileFieldNames.CREATED_AT to userProfile.createdAt,
            ProfileFieldNames.COINS to userProfile.coins
        )
    }
}
