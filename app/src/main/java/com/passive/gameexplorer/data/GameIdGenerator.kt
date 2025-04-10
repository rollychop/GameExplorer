package com.passive.gameexplorer.data

import android.util.Log
import com.passive.gameexplorer.model.GameModel
import java.util.UUID


// GameIdGenerator class to generate and manage game IDs
object GameIdGenerator {

    // Function to generate a random Game ID (dummy ID generation)
    private fun generateGameId(): String {
        return UUID.randomUUID().toString().take(8) // Generate a short 8 character ID
    }

    // Function to generate BGMI game details
    fun generateBgmiGameId(): GameModel {
        // BGMI-specific details
        val gameLevel = randomBgmiLevel()
        val gameGuns = randomBgmiGuns()
        val gameOutfits = randomBgmiOutfits()
        val gameVehicles = randomBgmiVehicles()
        val rating = (1..5).random().toFloat() // Random rating between 1 and 5

        val gameId = generateGameId()

        return GameModel(
            gameId = gameId,
            gameType = "BGMI",
            gameLevel = gameLevel,
            gameGuns = gameGuns,
            gameOutfits = gameOutfits,
            gameVehicles = gameVehicles,
            rating = rating,
            createdAt = System.currentTimeMillis(),
        )
    }

    // Function to generate Free Fire game details
    fun generateFreeFireGameId(): GameModel {
        // Free Fire-specific details
        val gameLevel = randomFreeFireLevel()
        val gameGuns = randomFreeFireGuns()
        val gameOutfits = randomFreeFireOutfits()
        val gameVehicles = randomFreeFireVehicles()
        val rating = (1..5).random().toFloat() // Random rating between 1 and 5

        val gameId = generateGameId()

        return GameModel(
            gameId = gameId,
            gameType = "Free Fire",
            gameLevel = gameLevel,
            gameGuns = gameGuns,
            gameOutfits = gameOutfits,
            gameVehicles = gameVehicles,
            rating = rating,
            createdAt = System.currentTimeMillis(),
        )
    }

    // BGMI Level Options
    private fun randomBgmiLevel(): String {
        return listOf(
            "Bronze 🥉",
            "Silver 🥈",
            "Gold 🏅",
            "Platinum 💎",
            "Diamond 💎",
            "Crown 👑",
            "Ace 🏆"
        ).random()
    }

    // BGMI Gun Options
    private fun randomBgmiGuns(): List<String> {
        return listOf(
            "AKM 🔫",
            "M416 🪓",
            "Scar-L 🔫",
            "AWM 🎯",
            "Kar98k 🔭"
        ).shuffled().take(3)
    }

    // BGMI Outfit Options
    private fun randomBgmiOutfits(): List<String> {
        return listOf(
            "Urban Warrior 👕",
            "Elite Soldier 🕵️‍♂️",
            "Jungle Camouflage 🏞️",
            "Tactical Gear 🎽",
            "Knight's Armor ⚔️"
        ).shuffled().take(2)
    }

    // BGMI Vehicle Options
    private fun randomBgmiVehicles(): List<String> {
        return listOf(
            "Dune Buggy 🚙",
            "UAZ 🛻",
            "Motorcycle 🏍️",
            "Vikendi Snowmobile ❄️",
            "Buggy 🌵"
        ).shuffled().take(2)
    }

    // Free Fire Level Options
    private fun randomFreeFireLevel(): String {
        return listOf(
            "Bronze 🥉",
            "Silver 🥈",
            "Gold 🏅",
            "Platinum 💎",
            "Diamond 💎",
            "Master 🏆",
            "Grandmaster 🏅"
        ).random()
    }

    // Free Fire Gun Options
    private fun randomFreeFireGuns(): List<String> {
        return listOf(
            "AK 🔫",
            "M1014 Shotgun 🦸‍♂️",
            "MP40 🕹️",
            "Groza 🔥",
            "AWM 🎯"
        ).shuffled().take(3)
    }

    // Free Fire Outfit Options
    private fun randomFreeFireOutfits(): List<String> {
        return listOf(
            "Mafia Boss 👔",
            "Fury Fighter 🥊",
            "Street King 👑",
            "Camo Sniper 🎯",
            "Tech Knight 🦸‍♂️"
        ).shuffled().take(2)
    }

    // Free Fire Vehicle Options
    private fun randomFreeFireVehicles(): List<String> {
        return listOf(
            "Motorbike 🏍️",
            "Jeep 🚙",
            "Buggy 🚗",
            "Monster Truck 🚚"
        ).shuffled().take(2)
    }

    // Function to log and view the generated game ID details (for debugging)
    fun logGeneratedGameId(gameType: String) {
        val gameIdDetails = when (gameType) {
            "BGMI" -> generateBgmiGameId()
            "Free Fire" -> generateFreeFireGameId()
            else -> {
                Log.e("GameIdGenerator", "Unsupported game type: $gameType")
                return
            }
        }
        Log.d("GameIdGenerator", "Generated ${gameType} Game ID: $gameIdDetails")
    }
}
