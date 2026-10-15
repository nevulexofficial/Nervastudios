package com.example.model

data class MatchCommentary(
    val minute: Int,
    val text: String,
    val isGoal: Boolean = false,
    val isCard: Boolean = false,
    val cardType: String? = null, // "YELLOW", "RED"
    val isPenalty: Boolean = false,
    val isImportant: Boolean = false
)

data class MatchStats(
    val homePossession: Int = 50,
    val awayPossession: Int = 50,
    val homeShots: Int = 0,
    val awayShots: Int = 0,
    val homeShotsOnTarget: Int = 0,
    val awayShotsOnTarget: Int = 0,
    val homeFouls: Int = 0,
    val awayFouls: Int = 0,
    val homeYellowCards: Int = 0,
    val awayYellowCards: Int = 0,
    val homeRedCards: Int = 0,
    val awayRedCards: Int = 0,
    val homePenalties: Int = 0,
    val awayPenalties: Int = 0,
    val homeCorners: Int = 0,
    val awayCorners: Int = 0
)

data class Match(
    val id: String,
    val round: Int,
    val homeClubId: String,
    val awayClubId: String,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val isPlayed: Boolean = false,
    val isCurrent: Boolean = false,
    val isSimulating: Boolean = false,
    val currentMinute: Int = 0,
    val commentaries: List<MatchCommentary> = emptyList(),
    val stats: MatchStats = MatchStats(),
    val homeScorers: List<String> = emptyList(),
    val awayScorers: List<String> = emptyList()
)
