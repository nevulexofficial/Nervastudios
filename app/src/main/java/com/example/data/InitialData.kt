package com.example.data

import com.example.model.*

object InitialData {

    val clubs = listOf(
        Club(
            id = "c_nerva",
            name = "NERVA FC",
            shortName = "NRV",
            managerName = "Tú (DT Nerva)",
            stadiumName = "Nerva Cyber Stadium",
            primaryColorHex = 0xFF0D3AE8,
            secondaryColorHex = 0xFF061026,
            budget = 12_450_000L,
            coins = 2276,
            weeklyWageBudget = 890_000L,
            reputation = 86,
            isUserClub = true,
            played = 6,
            won = 4,
            drawn = 1,
            lost = 1,
            goalsFor = 13,
            goalsAgainst = 6,
            points = 13,
            recentForm = listOf("W", "W", "D", "W", "L")
        ),
        Club(
            id = "c_chilenos",
            name = "Chilenos FC",
            shortName = "CHI",
            managerName = "El Profe Díaz",
            stadiumName = "Estadio Monumental Andino",
            primaryColorHex = 0xFFEF4444,
            secondaryColorHex = 0xFF1E3A8A,
            budget = 42_000_000L,
            weeklyWageBudget = 820_000L,
            reputation = 85,
            isUserClub = false,
            played = 6,
            won = 4,
            drawn = 2,
            lost = 0,
            goalsFor = 14,
            goalsAgainst = 5,
            points = 14,
            recentForm = listOf("W", "W", "W", "D", "W")
        ),
        Club(
            id = "c_nazionale",
            name = "La Nazionale",
            shortName = "NAZ",
            managerName = "Mister Rossi",
            stadiumName = "Stadio San Vittore",
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFFFFFFFF,
            budget = 52_000_000L,
            weeklyWageBudget = 950_000L,
            reputation = 87,
            isUserClub = false,
            played = 6,
            won = 4,
            drawn = 0,
            lost = 2,
            goalsFor = 11,
            goalsAgainst = 4,
            points = 12,
            recentForm = listOf("L", "W", "W", "W", "W")
        ),
        Club(
            id = "c_tamber",
            name = "Tamber CF",
            shortName = "TAM",
            managerName = "Tamber_Boss",
            stadiumName = "Coliseo Tamber",
            primaryColorHex = 0xFFEAB308,
            secondaryColorHex = 0xFF18181B,
            budget = 39_000_000L,
            weeklyWageBudget = 780_000L,
            reputation = 83,
            isUserClub = false,
            played = 6,
            won = 3,
            drawn = 2,
            lost = 1,
            goalsFor = 10,
            goalsAgainst = 7,
            points = 11,
            recentForm = listOf("W", "D", "W", "L", "D")
        ),
        Club(
            id = "c_porto",
            name = "Porto Azul",
            shortName = "POR",
            managerName = "Sergio Silva",
            stadiumName = "Arena do Dragão Azul",
            primaryColorHex = 0xFF2563EB,
            secondaryColorHex = 0xFF64748B,
            budget = 35_000_000L,
            weeklyWageBudget = 710_000L,
            reputation = 81,
            isUserClub = false,
            played = 6,
            won = 2,
            drawn = 2,
            lost = 2,
            goalsFor = 8,
            goalsAgainst = 8,
            points = 8,
            recentForm = listOf("D", "L", "W", "D", "L")
        ),
        Club(
            id = "c_capital",
            name = "Atlético Capital",
            shortName = "CAP",
            managerName = "Marcos V.",
            stadiumName = "Metropolitano Central",
            primaryColorHex = 0xFFDC2626,
            secondaryColorHex = 0xFFF8FAFC,
            budget = 33_000_000L,
            weeklyWageBudget = 690_000L,
            reputation = 82,
            isUserClub = false,
            played = 6,
            won = 2,
            drawn = 1,
            lost = 3,
            goalsFor = 7,
            goalsAgainst = 10,
            points = 7,
            recentForm = listOf("L", "W", "L", "D", "W")
        ),
        Club(
            id = "c_real",
            name = "Deportivo Real",
            shortName = "DEP",
            managerName = "Javier M.",
            stadiumName = "Santiago Park",
            primaryColorHex = 0xFF8B5CF6,
            secondaryColorHex = 0xFF1E293B,
            budget = 30_000_000L,
            weeklyWageBudget = 640_000L,
            reputation = 80,
            isUserClub = false,
            played = 6,
            won = 1,
            drawn = 1,
            lost = 4,
            goalsFor = 6,
            goalsAgainst = 13,
            points = 4,
            recentForm = listOf("L", "L", "D", "L", "W")
        ),
        Club(
            id = "c_valle",
            name = "Valle FC",
            shortName = "VAL",
            managerName = "Lucas R.",
            stadiumName = "Estadio Valle Verde",
            primaryColorHex = 0xFF10B981,
            secondaryColorHex = 0xFF064E3B,
            budget = 27_000_000L,
            weeklyWageBudget = 580_000L,
            reputation = 79,
            isUserClub = false,
            played = 6,
            won = 0,
            drawn = 1,
            lost = 5,
            goalsFor = 4,
            goalsAgainst = 15,
            points = 1,
            recentForm = listOf("L", "L", "L", "D", "L")
        )
    )

    val nervaPlayers = listOf(
        Player("p_n1", "Mateo Varela", 1, PositionType.POR, "POR", 26, "🇨🇱", "NERVA FC", 85, 62, 28, 74, 55, 87, 83, 98, "Excelente", 22_000_000L, 45_000L, "c_nerva", true, 0),
        Player("p_n2", "Julián Costa", 3, PositionType.DEF, "LI", 24, "🇨🇱", "NERVA FC", 82, 85, 58, 77, 79, 81, 76, 95, "Motivado", 18_000_000L, 38_000L, "c_nerva", true, 1),
        Player("p_n3", "Diego Alarcón", 4, PositionType.DEF, "DFC", 28, "🇨🇱", "NERVA FC", 86, 75, 45, 72, 68, 88, 89, 92, "Motivado", 26_000_000L, 52_000L, "c_nerva", true, 2),
        Player("p_n4", "Bautista Silva", 5, PositionType.DEF, "DFC", 27, "🇨🇱", "NERVA FC", 84, 76, 48, 75, 70, 85, 86, 94, "Motivado", 23_000_000L, 48_000L, "c_nerva", true, 3),
        Player("p_n5", "Thiago Mendes", 2, PositionType.DEF, "LD", 23, "🇧🇷", "NERVA FC", 81, 88, 60, 76, 80, 79, 74, 91, "Regular", 16_500_000L, 35_000L, "c_nerva", true, 4),
        Player("p_n6", "Lucas Kova", 6, PositionType.MED, "MCD", 29, "🇭🇷", "NERVA FC", 87, 74, 72, 89, 85, 84, 85, 96, "Excelente", 32_000_000L, 68_000L, "c_nerva", true, 5),
        Player("p_n7", "Álvaro Prieto", 8, PositionType.MED, "MC", 25, "🇪🇸", "NERVA FC", 85, 80, 81, 88, 87, 72, 77, 93, "Motivado", 29_000_000L, 58_000L, "c_nerva", true, 6),
        Player("p_n8", "Enzo Navarro", 10, PositionType.MED, "MCO", 22, "🇦🇷", "NERVA FC", 86, 84, 83, 90, 89, 65, 72, 97, "Excelente", 36_000_000L, 62_000L, "c_nerva", true, 7),
        Player("p_n9", "Facundo Bravo", 7, PositionType.DEL, "EI", 24, "🇨🇱", "NERVA FC", 84, 91, 84, 78, 88, 48, 73, 90, "Motivado", 27_000_000L, 50_000L, "c_nerva", true, 8, goalsSeason = 3, assistsSeason = 2),
        Player("p_n10", "Gabriel Nova", 9, PositionType.DEL, "DC", 25, "🇨🇱", "NERVA FC", 88, 86, 91, 79, 85, 42, 84, 94, "Excelente", 45_000_000L, 85_000L, "c_nerva", true, 9, goalsSeason = 6, assistsSeason = 1),
        Player("p_n11", "Santiago Rey", 11, PositionType.DEL, "ED", 23, "🇨🇱", "NERVA FC", 83, 90, 80, 81, 87, 45, 71, 92, "Motivado", 24_000_000L, 46_000L, "c_nerva", true, 10, goalsSeason = 2, assistsSeason = 4),
        // Bench
        Player("p_n12", "Tomás Ríos", 13, PositionType.POR, "POR", 22, "🇨🇱", "NERVA FC", 76, 58, 20, 68, 50, 77, 75, 100, "Regular", 6_000_000L, 18_000L, "c_nerva", false, -1),
        Player("p_n13", "Martín Cáceres", 15, PositionType.DEF, "DFC", 21, "🇺🇾", "NERVA FC", 78, 78, 42, 70, 68, 79, 80, 100, "Motivado", 8_500_000L, 22_000L, "c_nerva", false, -1),
        Player("p_n14", "Rodrigo Benítez", 16, PositionType.MED, "MC", 26, "🇨🇱", "NERVA FC", 79, 75, 74, 80, 78, 76, 78, 95, "Motivado", 11_000_000L, 26_000L, "c_nerva", false, -1),
        Player("p_n15", "Iván Delgado", 17, PositionType.MED, "MC", 20, "🇨🇱", "NERVA FC", 77, 82, 72, 79, 81, 60, 68, 100, "Motivado", 9_000_000L, 20_000L, "c_nerva", false, -1),
        Player("p_n16", "Franco Romero", 19, PositionType.DEL, "DC", 21, "🇦🇷", "NERVA FC", 79, 87, 80, 72, 81, 38, 74, 98, "Motivado", 12_500_000L, 25_000L, "c_nerva", false, -1, goalsSeason = 2),
        Player("p_n17", "Leandro Vega", 20, PositionType.DEL, "DC", 27, "🇨🇱", "NERVA FC", 80, 82, 82, 75, 79, 41, 79, 94, "Motivado", 14_000_000L, 28_000L, "c_nerva", false, -1)
    )

    // Exact market cards from Screenshot 174056.png
    val marketPool = listOf(
        Player("p_m_bellingham", "Jude Bellingham", 5, PositionType.MED, "MC", 21, "🏴󠁧󠁢󠁥󠁮󠁧󠁿", "Real Madrid", 88, 82, 78, 84, 88, 78, 83, 98, "Excelente", 85_000_000L, 160_000L, "c_real", false, -1, isTransferListed = true),
        Player("p_m_osimhen", "Victor Osimhen", 9, PositionType.DEL, "DC", 25, "🇳🇬", "Napoli", 86, 93, 86, 68, 82, 41, 82, 97, "Excelente", 70_000_000L, 140_000L, "c_porto", false, -1, isTransferListed = true),
        Player("p_m_wirtz", "Florian Wirtz", 10, PositionType.MED, "MCO", 22, "🇩🇪", "Bayer Leverkusen", 85, 82, 78, 86, 88, 56, 67, 96, "Excelente", 65_000_000L, 130_000L, "c_porto", false, -1, isTransferListed = true),
        Player("p_m_dias", "Rúben Dias", 3, PositionType.DEF, "DFC", 28, "🇵🇹", "Man City", 85, 66, 39, 70, 69, 86, 84, 95, "Excelente", 55_000_000L, 120_000L, "c_capital", false, -1, isTransferListed = true),
        Player("p_m_mbappe", "Kylian Mbappé", 9, PositionType.DEL, "DC", 26, "🇫🇷", "Real Madrid", 91, 97, 92, 86, 92, 36, 78, 99, "Excelente", 120_000_000L, 250_000L, "c_real", false, -1, isTransferListed = true),
        Player("p_m_valverde", "Federico Valverde", 8, PositionType.MED, "MC", 26, "🇺🇾", "Real Madrid", 84, 92, 88, 88, 82, 78, 82, 96, "Excelente", 50_000_000L, 110_000L, "c_real", false, -1, isTransferListed = true),
        Player("p_m_martinez", "Lautaro Martínez", 10, PositionType.DEL, "DC", 27, "🇦🇷", "Inter", 86, 82, 83, 85, 83, 45, 82, 95, "Excelente", 68_000_000L, 135_000L, "c_capital", false, -1, isTransferListed = true),
        Player("p_m_davies", "Alphonso Davies", 19, PositionType.DEF, "LI", 24, "🇨🇦", "Bayern", 84, 96, 72, 78, 85, 76, 78, 97, "Excelente", 60_000_000L, 125_000L, "c_valle", false, -1, isTransferListed = true),
        // Rival players
        Player("p_c1", "Alexis Valenzuela", 7, PositionType.DEL, "DC", 29, "🇨🇱", "Chilenos FC", 87, 89, 88, 86, 91, 55, 77, 95, "Excelente", 38_000_000L, 75_000L, "c_chilenos", true, 8, goalsSeason = 7, assistsSeason = 3),
        Player("p_c2", "Arturo Aránguiz", 8, PositionType.MED, "MC", 31, "🇨🇱", "Chilenos FC", 85, 74, 82, 85, 81, 86, 88, 90, "Motivado", 22_000_000L, 65_000L, "c_chilenos", true, 6),
        Player("p_nz1", "Gianluigi Donati", 1, PositionType.POR, "POR", 27, "🇮🇹", "La Nazionale", 89, 65, 30, 78, 58, 91, 88, 98, "Excelente", 45_000_000L, 88_000L, "c_nazionale", true, 0),
        Player("p_t1", "Mateo Tamber Jr.", 10, PositionType.MED, "MCO", 23, "🇧🇷", "Tamber CF", 86, 87, 85, 88, 90, 62, 73, 96, "Excelente", 39_000_000L, 70_000L, "c_tamber", true, 7, goalsSeason = 4, assistsSeason = 5)
    )

    val initialMatches = listOf(
        // Round 7 (Current upcoming round)
        Match(
            id = "m_r7_1",
            round = 7,
            homeClubId = "c_nerva",
            awayClubId = "c_chilenos",
            isPlayed = false,
            isCurrent = true
        ),
        Match(
            id = "m_r7_2",
            round = 7,
            homeClubId = "c_nazionale",
            awayClubId = "c_tamber",
            isPlayed = false
        ),
        Match(
            id = "m_r7_3",
            round = 7,
            homeClubId = "c_porto",
            awayClubId = "c_capital",
            isPlayed = false
        ),
        Match(
            id = "m_r7_4",
            round = 7,
            homeClubId = "c_real",
            awayClubId = "c_valle",
            isPlayed = false
        ),
        // Round 8
        Match(id = "m_r8_1", round = 8, homeClubId = "c_nerva", awayClubId = "c_nazionale", isPlayed = false),
        Match(id = "m_r8_2", round = 8, homeClubId = "c_chilenos", awayClubId = "c_tamber", isPlayed = false),
        // Past Round 6
        Match(
            id = "m_r6_1",
            round = 6,
            homeClubId = "c_nerva",
            awayClubId = "c_porto",
            homeScore = 3,
            awayScore = 1,
            isPlayed = true,
            homeScorers = listOf("Gabriel Nova (28')", "Gabriel Nova (64')", "Facundo Bravo (81')"),
            awayScorers = listOf("Sergio Silva Jr. (45')")
        ),
        Match(
            id = "m_r6_2",
            round = 6,
            homeClubId = "c_chilenos",
            awayClubId = "c_real",
            homeScore = 2,
            awayScore = 0,
            isPlayed = true,
            homeScorers = listOf("Alexis Valenzuela (14')", "Benjamín Soto (72')")
        )
    )

    val initialForumPosts = listOf(
        ForumPost(
            id = "fp_1",
            authorName = "El Profe Díaz",
            authorClubId = "c_chilenos",
            authorRole = "DT Chilenos FC (Líder)",
            title = "¡Listos para el partidazo de la Fecha 7 contra NERVA FC!",
            content = "Llegamos invictos a esta jornada. Sé que el míster de NERVA tiene un ataque temible con Nova y Bravo, pero nuestra zaga central está en su mejor momento. ¿Qué planteamiento van a usar contra nosotros? ¡Nos vemos en el campo!",
            category = "Partidos",
            timeAgo = "Hace 20 min",
            likesCount = 18,
            replies = listOf(
                ForumReply("fr_1", "Mister Rossi", "DT La Nazionale", "No te confíes Díaz, NERVA nos complicó con el juego por las bandas. Guarda piernas.", "Hace 15 min", 5),
                ForumReply("fr_2", "Tamber_Boss", "DT Tamber CF", "Un empate nos sirve a todos nosotros jajaja. ¡Que haya buen fútbol!", "Hace 8 min", 3)
            )
        ),
        ForumPost(
            id = "fp_2",
            authorName = "Mister Rossi",
            authorClubId = "c_nazionale",
            authorRole = "DT La Nazionale (3º)",
            title = "Debate táctico: ¿El Catenaccio 5-3-2 sigue vigente en ligas modernas?",
            content = "Muchos directores técnicos se obsesionan con el tiki-taka y la posesión estéril. En La Nazionale hemos encajado solo 4 goles en 6 fechas con repliegue bajo y salidas explosivas con Barella.",
            category = "Tácticas",
            timeAgo = "Hace 2 horas",
            likesCount = 29
        ),
        ForumPost(
            id = "fp_3",
            authorName = "Tamber_Boss",
            authorClubId = "c_tamber",
            authorRole = "DT Tamber CF (4º)",
            title = "Mercado abierto: Escucho ofertas por extremos rápidos",
            content = "Colegas DTs, tenemos presupuesto fresco tras la victoria de copa. Buscamos un extremo desequilibrante o mediapunta zurdo de OVR +83.",
            category = "Fichajes",
            timeAgo = "Hace 4 horas",
            likesCount = 14
        )
    )

    val initialIncomingOffers = listOf(
        TransferOffer(
            id = "to_1",
            playerId = "p_n11",
            playerName = "Santiago Rey",
            fromClubId = "c_tamber",
            fromClubName = "Tamber CF",
            toClubId = "c_nerva",
            toClubName = "NERVA FC",
            amount = 26_000_000L,
            playerWage = 55_000L,
            status = OfferStatus.PENDING,
            message = "Tamber_Boss ha enviado una oferta formal por tu extremo Santiago Rey. Consideran que encaja perfecto en su sistema 4-2-3-1."
        )
    )

    // Side list of recent transfers and sales from screenshot 174056.png
    data class RecentTransfer(
        val playerShort: String,
        val clubName: String,
        val clubBadgeText: String,
        val amountFormatted: String
    )

    val recentMarketPurchases = listOf(
        RecentTransfer("L. Martínez", "Inter", "INT", "$ 68.000.000"),
        RecentTransfer("J. Bellingham", "Real Madrid", "RMA", "$ 85.000.000"),
        RecentTransfer("K. Mbappé", "Real Madrid", "RMA", "$ 120.000.000")
    )

    val recentMarketSales = listOf(
        RecentTransfer("M. Ter Stegen", "Barcelona", "FCB", "$ 25.000.000"),
        RecentTransfer("F. Valverde", "Real Madrid", "RMA", "$ 50.000.000"),
        RecentTransfer("R. Leão", "Milan", "ACM", "$ 45.000.000")
    )

    val initialSponsors = listOf(
        Sponsor(
            id = "sp_aero",
            name = "AERO DYNAMICS GLOBAL",
            type = "Principal (Frontal)",
            logoTag = "AERO",
            weeklyIncome = 580_000L,
            winBonus = 160_000L,
            contractDurationMonths = 12,
            isSigned = true,
            description = "Patrocinador aeroespacial y tecnológico principal en la camiseta de NERVA FC."
        ),
        Sponsor(
            id = "sp_cyber",
            name = "CYBERNET MOTORS",
            type = "Manga",
            logoTag = "CYBER",
            weeklyIncome = 240_000L,
            winBonus = 60_000L,
            contractDurationMonths = 6,
            isSigned = true,
            description = "Líder en movilidad eléctrica y vehículos autónomos de alta competición."
        ),
        Sponsor(
            id = "sp_vortex",
            name = "VORTEX PERFORMANCE APPAREL",
            type = "Equipación Deportiva",
            logoTag = "VORTEX",
            weeklyIncome = 320_000L,
            winBonus = 90_000L,
            contractDurationMonths = 24,
            isSigned = true,
            description = "Fabricante oficial de indumentaria de alta tecnología transpirable."
        ),
        Sponsor(
            id = "sp_quantum",
            name = "QUANTUM CAPITAL BANK",
            type = "Estadio & Naming",
            logoTag = "QUANTUM",
            weeklyIncome = 750_000L,
            winBonus = 220_000L,
            contractDurationMonths = 36,
            isSigned = false,
            description = "Oferta disponible para el naming oficial del Nerva Cyber Stadium."
        ),
        Sponsor(
            id = "sp_redbull",
            name = "TITAN ENERGY DRINKS",
            type = "Bebida Oficial",
            logoTag = "TITAN",
            weeklyIncome = 180_000L,
            winBonus = 50_000L,
            contractDurationMonths = 12,
            isSigned = false,
            description = "Bebidas isotónicas y energéticas premium para el rendimiento de la plantilla."
        )
    )
}
