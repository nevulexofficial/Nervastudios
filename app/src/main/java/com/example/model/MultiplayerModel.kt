package com.example.model

enum class LobbyStatus {
    WAITING,
    READY,
    PLAYING,
    FINISHED,
    CANCELLED
}

data class MultiplayerLobby(
    val id: String,
    val roomCode: String,
    val hostManagerId: String,
    val hostClubName: String,
    val hostElo: Int = 1200,
    val guestManagerId: String? = null,
    val guestClubName: String? = null,
    val guestElo: Int? = null,
    val status: LobbyStatus = LobbyStatus.WAITING,
    val matchId: String? = null,
    val isPrivate: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class MultiplayerPresence(
    val managerId: String,
    val managerName: String,
    val clubName: String,
    val clubDivision: Int = 12,
    val eloRating: Int = 1200,
    val status: String = "online",
    val pingMs: Int = 32
)

data class MultiplayerLeaderboardEntry(
    val rank: Int,
    val managerId: String,
    val managerName: String,
    val clubName: String,
    val eloRating: Int,
    val matchesPlayed: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val divisionTier: String
)

data class MultiplayerMatchSync(
    val matchId: String,
    val lobbyId: String,
    val homeClubName: String,
    val awayClubName: String,
    val homeScore: Int,
    val awayScore: Int,
    val minute: Int,
    val second: Int,
    val isPlaying: Boolean,
    val isHalftime: Boolean,
    val isFinished: Boolean,
    val lastCommentary: String
)
