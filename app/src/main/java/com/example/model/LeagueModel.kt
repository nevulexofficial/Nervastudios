package com.example.model

data class League(
    val id: String,
    val name: String,
    val divisionsCount: Int = 12,
    val maxClubsPerDivision: Int = 30,
    val promotesFromLeagueId: String? = null,
    val bannerStyle: String = "CYBER_BLUE",
    val iconEmoji: String = "⚡",
    val tier: Int = 1,
    val description: String = "Liga principal donde inician todas las promesas y nuevos clubes. 12 divisiones con sistema de ascensos y descensos."
)

data class LeagueDivision(
    val divisionNumber: Int,
    val name: String,
    val leagueId: String,
    val clubs: List<Club>,
    val promotionSpots: Int = 3,
    val relegationSpots: Int = 3
) {
    val userClubIndex: Int
        get() = clubs.indexOfFirst { it.isUserClub }
}

data class ScheduledMatchSlot(
    val id: String,
    val hour: Int,
    val minute: Int = 0,
    val label: String, // e.g. "05:00 AM"
    val name: String,  // e.g. "Jornada del Amanecer"
    val lastSimulatedScore: String? = null,
    val isCompletedToday: Boolean = false
)

enum class AppBackgroundTheme(
    val title: String,
    val subtitle: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val previewIcon: String,
    val isLight: Boolean = false
) {
    NEGRO_ABSOLUTO(
        "Negro Puro OLED",
        "Estética oscura profunda AAA ultra contraste",
        0xFF121212,
        0xFF000000,
        "🖤",
        false
    ),
    BLANCO_LUMINOSO(
        "Blanco Nieve & Titanio",
        "Modo claro limpio, brillante y elegante",
        0xFFE2E8F0,
        0xFFF8FAFC,
        "🤍",
        true
    ),
    ESTADIO_NOCTURNO(
        "Estadio Nocturno",
        "Ambiente bajo los focos del Nerva Arena",
        0xFF0A192F,
        0xFF020C1B,
        "🏟️",
        false
    ),
    CYBER_NEON(
        "Cyber Neon 2026",
        "Gradiente eléctrico cian y azul profundo",
        0xFF0D2566,
        0xFF050B14,
        "⚡"
    ),
    CESPED_ESMERALDA(
        "Césped Esmeralda",
        "Verde intenso de campo de fútbol profesional",
        0xFF064E3B,
        0xFF022C22,
        "🌱"
    ),
    ORO_CHAMPIONS(
        "Oro Champions",
        "Negro carbón y dorado imperial de gala",
        0xFF78350F,
        0xFF18181B,
        "👑"
    ),
    CARBON_MODERN(
        "Titanio & Carbón",
        "Gris grafito moderno y minimalista",
        0xFF1E293B,
        0xFF0F172A,
        "🛡️"
    ),
    AURORA_PURPLE(
        "Aurora Cósmica",
        "Violeta y magenta galáctico estelar",
        0xFF4C1D95,
        0xFF1E1B4B,
        "🌌"
    )
}
