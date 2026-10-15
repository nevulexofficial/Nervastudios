package com.example.model

enum class PositionType(val displayName: String, val category: String) {
    POR("POR", "Portero"),
    DEF("DFC", "Defensa"),
    MED("MC", "Mediocampista"),
    DEL("DC", "Delantero")
}

data class SeasonEvolutionPoint(
    val matchday: Int, // Jornada 1 to 10
    val label: String, // "J1", "J2", etc.
    val marketValue: Long, // in Euros/Dollars
    val stamina: Int, // 50 to 100%
    val matchRating: Float = 7.0f,
    val note: String = ""
)

data class Player(
    val id: String,
    val name: String,
    val number: Int,
    val position: PositionType,
    val specificPos: String = "MC", // MC, DC, MCO, DFC, LI, LD, POR, EI, ED
    val age: Int,
    val nationality: String = "🇨🇱",
    val clubName: String = "NERVA FC",
    val overall: Int,
    val pace: Int,      // RIT
    val shooting: Int,  // TIR
    val passing: Int,   // PAS
    val dribbling: Int, // REG
    val defending: Int, // DEF
    val physical: Int,  // FIS
    val stamina: Int = 100,
    val morale: String = "Motivado",
    val marketValue: Long, // in Dollars/Euros
    val wage: Long = 45_000L,
    val clubId: String,
    val isStarter: Boolean = false,
    val lineupPosition: Int = -1,
    val goalsSeason: Int = 0,
    val assistsSeason: Int = 0,
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val isTransferListed: Boolean = false,
    val seasonHistory: List<SeasonEvolutionPoint> = emptyList()
) {
    val formattedValue: String
        get() = "$ " + String.format("%,d", marketValue).replace(",", ".")

    val formattedWage: String
        get() = "$ " + String.format("%,d", wage).replace(",", ".") + "/sem"

    /**
     * Generates a realistic 10-matchday progression curve based on player's overall, age and value.
     */
    fun getResolvedHistory(): List<SeasonEvolutionPoint> {
        if (seasonHistory.isNotEmpty()) return seasonHistory
        val baseVal = marketValue.toDouble()
        val result = mutableListOf<SeasonEvolutionPoint>()
        // 10 matchdays
        val matchdays = listOf("J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10")
        val valueFluctuations = listOf(0.92, 0.94, 0.91, 0.97, 1.00, 1.03, 1.01, 1.06, 1.04, 1.08)
        val staminaFluctuations = listOf(98, 92, 86, 94, 88, 95, 82, 90, 89, stamina.coerceIn(75, 100))
        val ratings = listOf(7.1f, 7.4f, 6.8f, 7.9f, 8.2f, 7.5f, 6.9f, 8.4f, 7.8f, (overall / 10f).coerceIn(6.5f, 9.5f))
        
        matchdays.forEachIndexed { i, label ->
            val vFactor = valueFluctuations[i]
            val valAtPoint = (baseVal * vFactor).toLong()
            val stamAtPoint = staminaFluctuations[i]
            val rating = ratings[i]
            result.add(
                SeasonEvolutionPoint(
                    matchday = i + 1,
                    label = label,
                    marketValue = valAtPoint,
                    stamina = stamAtPoint,
                    matchRating = rating,
                    note = when {
                        i == 7 -> "Gol decisivo & MVP"
                        i == 3 -> "Asistencia clave"
                        i == 6 -> "Fatiga por doble jornada"
                        else -> "Rendimiento óptimo"
                    }
                )
            )
        }
        return result
    }
}
