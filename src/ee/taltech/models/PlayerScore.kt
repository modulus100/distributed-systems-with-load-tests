package ee.taltech.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerScore(val playerId: Long, val score: Long)