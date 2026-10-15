package com.example.model

data class Club(
    val id: String,
    val name: String,
    val shortName: String,
    val managerName: String,
    val stadiumName: String,
    val primaryColorHex: Long = 0xFF0D3AE8,
    val secondaryColorHex: ColorHex = 0xFF061026,
    val budget: Long = 12_450_000L,
    val coins: Int = 2276,
    val weeklyWageBudget: Long = 850_000L,
    val reputation: Int = 85,
    val flag: String = "🇨🇱",
    val isUserClub: Boolean = false,
    val played: Int = 0,
    val won: Int = 0,
    val drawn: Int = 0,
    val lost: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0,
    val recentForm: List<String> = listOf("W", "W", "D", "W", "L"),
    // Advanced Club Customization
    val motto: String = "¡Hasta el Final, Nerva Real!",
    val badgeShape: String = "SHIELD", // SHIELD, CIRCLE, HEXAGON, DIAMOND, CREST
    val badgeSymbol: String = "EAGLE", // EAGLE, LION, CROWN, STAR, FOOTBALL, FIRE, LIGHTNING
    val badgeBorderColorHex: Long = 0xFF00F0FF,
    val leagueId: String = "gamma_nerva",
    val divisionNumber: Int = 12, // All new clubs start in Division 12 of Liga Gamma Nerva!
    val stadiumCapacity: Int = 45_000,
    val bannerStyle: String = "CYBER_NEON",
    val customLogoUri: String? = null
) {
    val goalDifference: Int
        get() = goalsFor - goalsAgainst

    val formattedBudget: String
        get() = "$ " + String.format("%,d", budget).replace(",", ".")
}

typealias ColorHex = Long
