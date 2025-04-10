package com.passive.gameexplorer.model

data class GameDetailModel(
    val gameModel: GameModel,
    val comments: List<CommentModel> = emptyList(),
    val ratings: List<RatingModel> = emptyList(),
    val userRating: RatingModel? = null,
    val userComment: CommentModel? = null
)

