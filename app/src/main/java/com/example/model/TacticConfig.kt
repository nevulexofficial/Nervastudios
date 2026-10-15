package com.example.model

enum class Formation(val displayName: String, val defCount: Int, val midCount: Int, val fwdCount: Int) {
    F_433("4-3-3", 4, 3, 3),
    F_442("4-4-2", 4, 4, 2),
    F_4231("4-2-3-1", 4, 5, 1),
    F_352("3-5-2", 3, 5, 2),
    F_532("5-3-2", 5, 3, 2)
}

enum class PlayStyle(val displayName: String, val description: String) {
    TIKI_TAKA("Tiki-Taka", "Posesión paciente, pases cortos y desmarques rápidos."),
    COUNTER_ATTACK("Contraataque Rápido", "Líneas compactas y salidas supersónicas a la espalda rival."),
    GEGENPRESS("Presión Alta (Gegenpress)", "Acoso asfixiante tras pérdida y recuperación en campo contrario."),
    DIRECT_PLAY("Juego Directo", "Transiciones verticales hacia los delanteros de referencia."),
    CATENACCIO("Catenaccio Férreo", "Bloque bajo impenetrable y defensa al hombre.")
}

enum class Mentality(val displayName: String, val attackBoost: Float, val defenseBoost: Float) {
    VERY_DEFENSIVE("Muy Defensiva", 0.6f, 1.4f),
    DEFENSIVE("Defensiva", 0.8f, 1.2f),
    BALANCED("Equilibrada", 1.0f, 1.0f),
    OFFENSIVE("Ofensiva", 1.2f, 0.8f),
    ALL_OUT_ATTACK("Al Ataque Total", 1.45f, 0.6f)
}

enum class PressingIntensity(val displayName: String) {
    LOW("Baja (Esperar en bloque)"),
    MEDIUM("Media (Zona media)"),
    HIGH("Asfixiante (En todo el campo)")
}

data class PitchSlot(
    val index: Int,
    val role: String,
    val defaultPosition: PositionType,
    val xNormalized: Float, // 0f (left) to 1f (right)
    val yNormalized: Float  // 0f (top / attacking) to 1f (bottom / goalkeeper)
)

data class TacticConfig(
    val formation: Formation = Formation.F_433,
    val playStyle: PlayStyle = PlayStyle.TIKI_TAKA,
    val mentality: Mentality = Mentality.BALANCED,
    val pressing: PressingIntensity = PressingIntensity.HIGH,
    val passingDirectness: Float = 0.35f, // 0: short passes, 1: direct long passes
    val captainPlayerId: String? = null,
    val penaltyTakerId: String? = null,
    val freeKickTakerId: String? = null
) {
    companion object {
        fun getSlotsForFormation(formation: Formation): List<PitchSlot> {
            return when (formation) {
                Formation.F_433 -> listOf(
                    PitchSlot(0, "POR", PositionType.POR, 0.50f, 0.90f),
                    PitchSlot(1, "LI", PositionType.DEF, 0.16f, 0.74f),
                    PitchSlot(2, "DFC", PositionType.DEF, 0.38f, 0.78f),
                    PitchSlot(3, "DFC", PositionType.DEF, 0.62f, 0.78f),
                    PitchSlot(4, "LD", PositionType.DEF, 0.84f, 0.74f),
                    PitchSlot(5, "MCD", PositionType.MED, 0.50f, 0.56f),
                    PitchSlot(6, "MC", PositionType.MED, 0.30f, 0.46f),
                    PitchSlot(7, "MC", PositionType.MED, 0.70f, 0.46f),
                    PitchSlot(8, "EI", PositionType.DEL, 0.18f, 0.24f),
                    PitchSlot(9, "DC", PositionType.DEL, 0.50f, 0.18f),
                    PitchSlot(10, "ED", PositionType.DEL, 0.82f, 0.24f)
                )
                Formation.F_442 -> listOf(
                    PitchSlot(0, "POR", PositionType.POR, 0.50f, 0.90f),
                    PitchSlot(1, "LI", PositionType.DEF, 0.16f, 0.74f),
                    PitchSlot(2, "DFC", PositionType.DEF, 0.38f, 0.78f),
                    PitchSlot(3, "DFC", PositionType.DEF, 0.62f, 0.78f),
                    PitchSlot(4, "LD", PositionType.DEF, 0.84f, 0.74f),
                    PitchSlot(5, "MI", PositionType.MED, 0.16f, 0.48f),
                    PitchSlot(6, "MC", PositionType.MED, 0.38f, 0.52f),
                    PitchSlot(7, "MC", PositionType.MED, 0.62f, 0.52f),
                    PitchSlot(8, "MD", PositionType.MED, 0.84f, 0.48f),
                    PitchSlot(9, "DC", PositionType.DEL, 0.38f, 0.20f),
                    PitchSlot(10, "DC", PositionType.DEL, 0.62f, 0.20f)
                )
                Formation.F_4231 -> listOf(
                    PitchSlot(0, "POR", PositionType.POR, 0.50f, 0.90f),
                    PitchSlot(1, "LI", PositionType.DEF, 0.16f, 0.74f),
                    PitchSlot(2, "DFC", PositionType.DEF, 0.38f, 0.78f),
                    PitchSlot(3, "DFC", PositionType.DEF, 0.62f, 0.78f),
                    PitchSlot(4, "LD", PositionType.DEF, 0.84f, 0.74f),
                    PitchSlot(5, "MCD", PositionType.MED, 0.36f, 0.60f),
                    PitchSlot(6, "MCD", PositionType.MED, 0.64f, 0.60f),
                    PitchSlot(7, "MI", PositionType.MED, 0.18f, 0.36f),
                    PitchSlot(8, "MCO", PositionType.MED, 0.50f, 0.34f),
                    PitchSlot(9, "MD", PositionType.MED, 0.82f, 0.36f),
                    PitchSlot(10, "DC", PositionType.DEL, 0.50f, 0.18f)
                )
                Formation.F_352 -> listOf(
                    PitchSlot(0, "POR", PositionType.POR, 0.50f, 0.90f),
                    PitchSlot(1, "DFC", PositionType.DEF, 0.25f, 0.76f),
                    PitchSlot(2, "LIB", PositionType.DEF, 0.50f, 0.80f),
                    PitchSlot(3, "DFC", PositionType.DEF, 0.75f, 0.76f),
                    PitchSlot(4, "CAI", PositionType.MED, 0.12f, 0.50f),
                    PitchSlot(5, "MC", PositionType.MED, 0.35f, 0.56f),
                    PitchSlot(6, "MCO", PositionType.MED, 0.50f, 0.42f),
                    PitchSlot(7, "MC", PositionType.MED, 0.65f, 0.56f),
                    PitchSlot(8, "CAD", PositionType.MED, 0.88f, 0.50f),
                    PitchSlot(9, "DC", PositionType.DEL, 0.38f, 0.20f),
                    PitchSlot(10, "DC", PositionType.DEL, 0.62f, 0.20f)
                )
                Formation.F_532 -> listOf(
                    PitchSlot(0, "POR", PositionType.POR, 0.50f, 0.90f),
                    PitchSlot(1, "LI", PositionType.DEF, 0.12f, 0.74f),
                    PitchSlot(2, "DFC", PositionType.DEF, 0.32f, 0.80f),
                    PitchSlot(3, "DFC", PositionType.DEF, 0.50f, 0.82f),
                    PitchSlot(4, "DFC", PositionType.DEF, 0.68f, 0.80f),
                    PitchSlot(5, "LD", PositionType.DEF, 0.88f, 0.74f),
                    PitchSlot(6, "MC", PositionType.MED, 0.28f, 0.52f),
                    PitchSlot(7, "MCD", PositionType.MED, 0.50f, 0.58f),
                    PitchSlot(8, "MC", PositionType.MED, 0.72f, 0.52f),
                    PitchSlot(9, "DC", PositionType.DEL, 0.38f, 0.22f),
                    PitchSlot(10, "DC", PositionType.DEL, 0.62f, 0.22f)
                )
            }
        }
    }
}
