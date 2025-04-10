package com.passive.gameexplorer.model

data class GameModel(
    val gameId: String,
    val gameType: String,         // e.g., "BGMI", "Free Fire"
    val gameLevel: String,        // e.g., "Pro", "Noob", "Legend"
    val gameGuns: List<String>,   // e.g., ["M416", "AWM"]
    val gameOutfits: List<String>,// e.g., ["Ghilli Suit", "Cyber Ninja"]
    val gameVehicles: List<String>, // e.g., ["Dacia", "Buggy"]
    val createdAt: Long,           // Timestamp
    val rating: Float,              // Average rating (can be calculated server-side),
    val totalComments: Int = 0,
    val profileUrl: String? = null,
    val screenShots: List<String> = emptyList(),
)