package com.passive.gameexplorer.model

data class UserProfile(
    val deviceId: String = "",
    val language: String? = null,
    val gameLevel: String? = null,
    val favoriteGame: String? = null,
    val topPlayer: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val coins: Int = 0,
)
