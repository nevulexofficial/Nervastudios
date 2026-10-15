package com.example.data

import com.example.model.Club
import com.example.model.League
import com.example.model.LeagueDivision
import com.example.model.ScheduledMatchSlot

object GammaLeagueData {

    val gammaLeague = League(
        id = "gamma_nerva",
        name = "Liga Gamma Nerva",
        divisionsCount = 12,
        maxClubsPerDivision = 30,
        promotesFromLeagueId = "Liga Delta Regional",
        bannerStyle = "CYBER_NEON",
        iconEmoji = "⚡",
        tier = 1,
        description = "La liga oficial de NERVA FC. Todo nuevo jugador empieza en la División 12. Cuenta con 12 divisiones de 30 clubes compitiendo por el ascenso."
    )

    val defaultScheduledSlots = listOf(
        ScheduledMatchSlot(
            id = "slot_5am",
            hour = 5,
            minute = 0,
            label = "05:00 AM",
            name = "Jornada del Amanecer",
            lastSimulatedScore = "NERVA FC 2 - 0 Andes Juniors",
            isCompletedToday = true
        ),
        ScheduledMatchSlot(
            id = "slot_10am",
            hour = 10,
            minute = 0,
            label = "10:00 AM",
            name = "Jornada Matutina Oficial",
            lastSimulatedScore = "NERVA FC 3 - 1 Chilenos FC",
            isCompletedToday = true
        ),
        ScheduledMatchSlot(
            id = "slot_3pm",
            hour = 15,
            minute = 0,
            label = "03:00 PM",
            name = "Jornada de la Tarde",
            lastSimulatedScore = null,
            isCompletedToday = false
        ),
        ScheduledMatchSlot(
            id = "slot_8pm",
            hour = 20,
            minute = 0,
            label = "08:00 PM",
            name = "Jornada Estelar Nocturna",
            lastSimulatedScore = null,
            isCompletedToday = false
        )
    )

    private val divisionNameSuffixes = listOf(
        "Élite Gamma",          // 1
        "Super Gamma",          // 2
        "Master Gamma",         // 3
        "Premier Gamma",        // 4
        "Nacional Gamma",       // 5
        "Regional Oro",         // 6
        "Regional Plata",       // 7
        "Ascenso Gamma",        // 8
        "Desafío Gamma",        // 9
        "Aspirantes Gamma",     // 10
        "Iniciación Gamma",     // 11
        "Promesas Gamma (División Inicial)" // 12
    )

    private val clubPrefixes = listOf(
        "Atlético", "Deportivo", "Real", "Cyber", "Sporting", "Unión", "Racing", "Inter",
        "Estrella", "Titanes", "Coliseo", "Halcones", "Gladiadores", "Dinamo", "Furia",
        "Vanguardia", "Imperio", "Sol de", "Águilas de", "Toros de", "Rayo", "Fénix"
    )

    private val clubLocations = listOf(
        "Nerva", "Santiago", "Valparaíso", "Biobío", "Andes", "Pacífico", "Córdoba", "Atacama",
        "Metrópolis", "Antofagasta", "Patagonia", "Arica", "Coquimbo", "Maule", "Araucanía",
        "Magallanes", "O'Higgins", "Tarapacá", "Austral", "Central", "Norte", "Sur",
        "Nevada", "Horizonte", "Glaciar", "Cordillera", "Aurora", "Delta", "Centinela"
    )

    private val flagList = listOf("🇨🇱", "🇦🇷", "🇧🇷", "🇺🇾", "🇨🇴", "🇵🇪", "🇲🇽", "🇪🇸", "🇩🇪", "🇫🇷", "🇮🇹", "🇬🇧")

    fun generate12Divisions(userClub: Club): Map<Int, LeagueDivision> {
        val divisionMap = mutableMapOf<Int, LeagueDivision>()

        for (divNum in 1..12) {
            val divName = "División $divNum — ${divisionNameSuffixes.getOrElse(divNum - 1) { "Gamma" }}"
            val clubsInDiv = mutableListOf<Club>()

            if (divNum == 12) {
                // User club belongs in Division 12 (all new players start here!)
                clubsInDiv.add(userClub.copy(divisionNumber = 12, isUserClub = true))

                // Add existing rivals first
                InitialData.clubs.filter { !it.isUserClub }.forEach {
                    clubsInDiv.add(it.copy(divisionNumber = 12, isUserClub = false))
                }

                // Fill up to 30 clubs
                var index = clubsInDiv.size + 1
                while (clubsInDiv.size < 30) {
                    val prefix = clubPrefixes[(index * 3) % clubPrefixes.size]
                    val loc = clubLocations[(index * 5) % clubLocations.size]
                    val name = "$prefix $loc"
                    val short = (prefix.take(2) + loc.take(1)).uppercase()
                    val flag = flagList[index % flagList.size]

                    // Calculate balanced points descending for realistic table
                    val pts = (30 - clubsInDiv.size) * 2 + (index % 3)
                    val played = 6
                    val won = pts / 3
                    val drawn = pts % 3
                    val lost = (played - won - drawn).coerceAtLeast(0)

                    clubsInDiv.add(
                        Club(
                            id = "c_div12_$index",
                            name = name,
                            shortName = short,
                            managerName = "DT $loc",
                            stadiumName = "Estadio $loc Arena",
                            primaryColorHex = 0xFF000000L or ((index * 458923L) and 0xFFFFFFL),
                            secondaryColorHex = 0xFF061026L,
                            budget = 8_000_000L + (index * 250_000L),
                            reputation = 65 + (index % 15),
                            flag = flag,
                            isUserClub = false,
                            played = played,
                            won = won,
                            drawn = drawn,
                            lost = lost,
                            goalsFor = won * 2 + drawn,
                            goalsAgainst = lost * 2 + drawn,
                            points = pts,
                            divisionNumber = 12
                        )
                    )
                    index++
                }
            } else {
                // Divisions 1 to 11 (each has 30 clubs)
                for (cIdx in 1..30) {
                    val prefix = clubPrefixes[(cIdx + divNum * 7) % clubPrefixes.size]
                    val loc = clubLocations[(cIdx * 2 + divNum * 3) % clubLocations.size]
                    val name = "$prefix $loc"
                    val short = (prefix.take(2) + loc.take(1)).uppercase()
                    val flag = flagList[(cIdx + divNum) % flagList.size]

                    val pts = (31 - cIdx) * 2 + (divNum % 2)
                    val played = 6
                    val won = pts / 3
                    val drawn = pts % 3
                    val lost = (played - won - drawn).coerceAtLeast(0)

                    clubsInDiv.add(
                        Club(
                            id = "c_div${divNum}_$cIdx",
                            name = name,
                            shortName = short,
                            managerName = "Mister $loc",
                            stadiumName = "Parque $loc",
                            primaryColorHex = 0xFF000000L or ((cIdx * 762391L + divNum * 12345L) and 0xFFFFFFL),
                            secondaryColorHex = 0xFF0D1B2A,
                            budget = (20_000_000L - (divNum * 1_000_000L)) + (cIdx * 300_000L),
                            reputation = 95 - (divNum * 2),
                            flag = flag,
                            isUserClub = false,
                            played = played,
                            won = won,
                            drawn = drawn,
                            lost = lost,
                            goalsFor = won * 2 + 1,
                            goalsAgainst = lost * 2,
                            points = pts,
                            divisionNumber = divNum
                        )
                    )
                }
            }

            // Sort by points descending
            val sortedClubs = clubsInDiv.sortedWith(
                compareByDescending<Club> { it.points }
                    .thenByDescending { it.goalDifference }
                    .thenByDescending { it.goalsFor }
            )

            divisionMap[divNum] = LeagueDivision(
                divisionNumber = divNum,
                name = divName,
                leagueId = "gamma_nerva",
                clubs = sortedClubs,
                promotionSpots = 3,
                relegationSpots = 3
            )
        }

        return divisionMap
    }
}
