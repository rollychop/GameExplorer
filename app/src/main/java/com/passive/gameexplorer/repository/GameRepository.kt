@file:Suppress("UNCHECKED_CAST")

package com.passive.gameexplorer.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.passive.gameexplorer.model.CommentModel
import com.passive.gameexplorer.model.GameDetailModel
import com.passive.gameexplorer.model.GameModel
import com.passive.gameexplorer.model.RatingModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

private const val TAG = "GameRepository"

class GameRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val gameIds = db.collection("game_ids")
    private val profiles = db.collection("profiles")
    private val ratings = db.collection("ratings")
    private val comments = db.collection("comments")

    // Method to save a generated Game ID to Firestore
    suspend fun saveGameId(gameModel: GameModel): Result<String> {
        return try {
            val gameRef = gameIds.document(gameModel.gameType.lowercase()).collection("ids")
                .document(gameModel.gameId)

            // Create a map of the game details to save
            val gameData = mapOf(
                "gameId" to gameModel.gameId,
                "gameType" to gameModel.gameType,
                "gameLevel" to gameModel.gameLevel,
                "gameGuns" to gameModel.gameGuns,
                "gameOutfits" to gameModel.gameOutfits,
                "gameVehicles" to gameModel.gameVehicles,
                "rating" to gameModel.rating,
                "createdAt" to FieldValue.serverTimestamp()
            )

            // Save to Firestore
            gameRef.set(gameData).await()

            Result.success("Game ID successfully saved")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Method to retrieve game IDs from Firestore based on game type
    suspend fun getGameIds(gameType: String): Result<List<GameModel>> {
        return try {
            val querySnapshot =
                gameIds.document(gameType.lowercase()).collection("ids").get().await()

            if (querySnapshot.isEmpty) {
                return Result.success(emptyList())
            }

            val gameIdsList = querySnapshot.documents.map { document ->
                gameModel(document)
            }

            Result.success(gameIdsList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun gameModel(document: DocumentSnapshot) = GameModel(
        gameId = document.getString("gameId") ?: "",
        gameType = document.getString("gameType") ?: "",
        gameLevel = document.getString("gameLevel") ?: "",
        gameGuns = document.get("gameGuns") as? List<String> ?: emptyList(),
        gameOutfits = document.get("gameOutfits") as? List<String> ?: emptyList(),
        gameVehicles = document.get("gameVehicles") as? List<String> ?: emptyList(),
        rating = (document.getDouble("rating") ?: 0).toFloat(),
        createdAt = (document.getDate("createdAt")?.time ?: 0),
        totalComments = (document.getLong("total_comments") ?: 0).toInt(),
        profileUrl = document.getString("profileUrl"),
        screenShots = document.get("screenShots") as? List<String> ?: emptyList(),
        extras = document.get("extras") as? List<String> ?: emptyList()
    )

    suspend fun getGameDetails(
        deviceId: String,
        gameId: String,
        gameType: String
    ): Result<GameDetailModel> {
        return try {

            val querySnapshot = gameIds.document(gameType.lowercase())
                .collection("ids")
                .document(gameId)
                .get()

            val commentsQuery = comments
                .whereEqualTo("gameId", gameId)
                .whereEqualTo("gameType", gameType)
                .get()
                .await()

            val ratingsQuery = ratings.whereEqualTo("gameId", gameId)
                .whereEqualTo("gameType", gameType)
                .whereEqualTo("deviceId", deviceId)
                .get()
                .await()

            val commentModels = if (commentsQuery.isEmpty) emptyList() else commentsQuery.documents
                .map { gameComment(it) }
            val ratingModels = if (ratingsQuery.isEmpty) emptyList() else ratingsQuery.documents
                .map { gameRating(it) }
            val gameDetails = GameDetailModel(
                gameModel = gameModel(querySnapshot.await()),
                comments = commentModels.filterNot { it.deviceId == deviceId },
                ratings = ratingModels.filterNot { it.deviceId == deviceId },
                userComment = commentModels.find { it.deviceId == deviceId },
                userRating = ratingModels.find { it.deviceId == deviceId }
            )

            Result.success(gameDetails)
        } catch (ex: Exception) {
            Result.failure(ex)
        }

    }


    private fun gameRating(document: DocumentSnapshot): RatingModel {
        return RatingModel(
            deviceId = document.getString("deviceId")
                ?: throw FirebaseFirestoreException(
                    "DeviceId not found",
                    FirebaseFirestoreException.Code.NOT_FOUND
                ),
            rating = (document.getLong("rating") ?: 0L).toInt(),
            createdAt = (document.getDate("created_at")?.time ?: 0L),
        )
    }

    private fun gameComment(document: DocumentSnapshot): CommentModel {
        return CommentModel(
            deviceId = document.getString("deviceId")
                ?: throw FirebaseFirestoreException(
                    "DeviceId not found",
                    FirebaseFirestoreException.Code.NOT_FOUND
                ),
            comment = document.getString("comment") ?: "",
            createdAt = (document.getDate("created_at")?.time ?: 0),
        )
    }

    // Method to update the rating for a game ID (if needed)
    suspend fun rateGameId(
        deviceId: String,
        gameId: String,
        gameType: String,
        rating: Int
    ): Result<String> {

        if (rating < 1) return Result.failure(Exception("Zero rating is not allowed"))
        Log.d(TAG, "rateGameId: $deviceId $gameId $gameType")
        return try {
            db.runTransaction { transaction ->
                runBlocking {
                    // Check if already rated by the user
                    val existingRating = ratings
                        .whereEqualTo("deviceId", deviceId)
                        .whereEqualTo("gameId", gameId)
                        .whereEqualTo("gameType", gameType)
                        .get()
                        .await()

                    if (!existingRating.isEmpty) {
                        throw FirebaseFirestoreException(
                            "Already rated",
                            FirebaseFirestoreException.Code.ABORTED
                        )
                    }

                    val userRef = profiles.document(deviceId)
                    val userSnap = transaction.get(userRef)
                    val currentCoins = userSnap.getLong("coins") ?: 0
                    Log.d(TAG, "rateGameId: ${userSnap.data}")

                    val gameRef = gameIds.document(gameType.lowercase())
                        .collection("ids")
                        .document(gameId)
                    val gameSnap = transaction.get(gameRef)
                    val ratingSum = gameSnap.getLong("rating_sum") ?: 0
                    val ratingCount = gameSnap.getLong("rating_count") ?: 0

                    // Add new rating
                    val ratingData = hashMapOf(
                        "deviceId" to deviceId,
                        "gameId" to gameId,
                        "gameType" to gameType,
                        "rating" to rating,
                        "created_at" to FieldValue.serverTimestamp()
                    )
                    transaction.set(ratings.document(), ratingData)
                    transaction.update(userRef, "coins", currentCoins + 10) // Reward 10 coins
                    // Update game ratings
                    transaction.update(
                        gameRef, mapOf(
                            "rating_sum" to (ratingSum + rating),
                            "rating_count" to (ratingCount + 1)
                        )
                    )
                    null
                }
            }.await()

            Result.success("Rating added and 10 coins rewarded")

        } catch (e: Exception) {
            Log.e(TAG, "rateGameId: ", e)
            Result.failure(e)
        }
    }

    // Method to comment on a Game ID
    suspend fun commentOnGameId(
        deviceId: String,
        gameId: String,
        gameType: String,
        comment: String
    ): Result<String> {
        if (comment.trim()
                .isEmpty()
        ) return Result.failure(Exception("Empty comment is not allowed"))


        return try {
            db.runTransaction { transaction ->

                // Deduct 10 coins from user for commenting
                val userRef = profiles.document(deviceId)
                val userSnap = transaction.get(userRef)
                val currentCoins = userSnap.getLong("coins") ?: 0
                val totalComments = userSnap.getLong("total_comments") ?: 0

                if (currentCoins < 10) {
                    throw FirebaseFirestoreException(
                        "Insufficient coins",
                        FirebaseFirestoreException.Code.ABORTED
                    )
                }

                // Deduct coins
                transaction.update(
                    userRef,
                    "coins",
                    currentCoins - 10,
                    "total_comments",
                    totalComments + 1
                )
                // Add comment
                val commentData = mapOf(
                    "deviceId" to deviceId,
                    "gameId" to gameId,
                    "gameType" to gameType,
                    "comment" to comment,
                    "created_at" to FieldValue.serverTimestamp()
                )
                transaction.set(comments.document(), commentData)

                null
            }.await()

            Result.success("Comment posted and 10 coins deducted")

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
