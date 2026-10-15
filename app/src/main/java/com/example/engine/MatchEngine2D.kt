package com.example.engine

import com.example.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.*
import kotlin.random.Random

data class Player2D(
    val id: String,
    val name: String,
    val number: Int,
    val isHomeTeam: Boolean,
    val positionType: PositionType,
    var x: Float, // 0.0f (left) to 1.0f (right)
    var y: Float, // 0.0f (top / home defending) to 1.0f (bottom / home attacking)
    val baseAnchorX: Float,
    val baseAnchorY: Float,
    val overall: Int,
    val pace: Int,
    val shooting: Int,
    val defending: Int,
    val passing: Int,
    var aiThought: String = "Posicionándose",
    var stamina: Float = 100f,
    var yellowCards: Int = 0,
    var isRedCarded: Boolean = false,
    var isCelebrating: Boolean = false
)

enum class BallState {
    CONTROLLED,
    PASSING,
    SHOOTING,
    GOAL_CELEBRATION,
    KICKOFF,
    FOUL_FREE_KICK,
    PENALTY_KICK
}

data class Ball2D(
    var x: Float = 0.5f,
    var y: Float = 0.5f,
    var targetX: Float = 0.5f,
    var targetY: Float = 0.5f,
    var state: BallState = BallState.KICKOFF,
    var holderId: String? = null,
    var speed: Float = 0.04f,
    var height: Float = 0.0f
)

data class MatchEngineState(
    val minute: Int = 0,
    val second: Int = 0,
    val currentHalf: Int = 1, // 1: 0'-15', 2: 15'-30'
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val isPlaying: Boolean = false,
    val isFinished: Boolean = false,
    val isHalftime: Boolean = false,
    val speedMultiplier: Int = 1, // 1, 2, 4
    val ball: Ball2D = Ball2D(),
    val players: List<Player2D> = emptyList(),
    val commentaries: List<MatchCommentary> = emptyList(),
    val stats: MatchStats = MatchStats(),
    val homeScorers: List<String> = emptyList(),
    val awayScorers: List<String> = emptyList(),
    val lastActionDescription: String = "Listos para el saque inicial.",
    val bannerEventTitle: String? = null,
    val celebrationCountdown: Int = 0,
    val addedStoppageMinutes: Int = 0,
    val isInStoppageTime: Boolean = false,
    val stoppageBoardNotice: String? = null
) {
    val formattedClock: String
        get() {
            val secStr = second.toString().padStart(2, '0')
            return if (isInStoppageTime) {
                val base = if (currentHalf == 1) 15 else 30
                val extraMin = (minute - base).coerceAtLeast(0)
                "${base}:00 +${extraMin}' ($secStr\")"
            } else {
                "${minute}:${secStr}"
            }
        }
}

class MatchEngine2D(
    val homeClub: Club,
    val awayClub: Club,
    val homePlayers: List<Player>,
    val awayPlayers: List<Player>,
    var mentality: Mentality = Mentality.BALANCED
) {
    private val _engineState = MutableStateFlow(MatchEngineState())
    val engineState: StateFlow<MatchEngineState> = _engineState.asStateFlow()

    private var simulationJob: Job? = null
    private val random = Random(System.currentTimeMillis())

    // 30 min match configuration
    private val half1RegulationMin = 15
    private val half2RegulationMin = 30
    private var half1Stoppage = 2
    private var half2Stoppage = 4

    init {
        half1Stoppage = random.nextInt(1, 4) // 1 to 3 minutes
        half2Stoppage = random.nextInt(2, 6) // 2 to 5 minutes
        initializePlayers()
    }

    private fun initializePlayers() {
        val playersList = mutableListOf<Player2D>()

        // 11 Home Players
        val homeStarters = homePlayers.filter { it.isStarter }.take(11).ifEmpty { homePlayers.take(11) }
        val homeSlots = listOf(
            Pair(0.50f, 0.10f), // POR
            Pair(0.18f, 0.24f), Pair(0.40f, 0.20f), Pair(0.60f, 0.20f), Pair(0.82f, 0.24f), // DEF
            Pair(0.50f, 0.36f), Pair(0.30f, 0.44f), Pair(0.70f, 0.44f), // MED
            Pair(0.24f, 0.60f), Pair(0.50f, 0.64f), Pair(0.76f, 0.60f)  // DEL
        )

        homeStarters.forEachIndexed { idx, p ->
            val slot = homeSlots.getOrElse(idx) { Pair(0.5f, 0.3f) }
            playersList.add(
                Player2D(
                    id = p.id,
                    name = p.name,
                    number = p.number,
                    isHomeTeam = true,
                    positionType = p.position,
                    x = slot.first,
                    y = slot.second,
                    baseAnchorX = slot.first,
                    baseAnchorY = slot.second,
                    overall = p.overall,
                    pace = p.pace,
                    shooting = p.shooting,
                    defending = p.defending,
                    passing = p.passing,
                    aiThought = "Preparado en posición"
                )
            )
        }

        // 11 Away Players
        val awayStarters = awayPlayers.filter { it.isStarter }.take(11).ifEmpty { awayPlayers.take(11) }
        val awaySlots = listOf(
            Pair(0.50f, 0.90f), // POR
            Pair(0.18f, 0.76f), Pair(0.40f, 0.80f), Pair(0.60f, 0.80f), Pair(0.82f, 0.76f), // DEF
            Pair(0.50f, 0.64f), Pair(0.30f, 0.56f), Pair(0.70f, 0.56f), // MED
            Pair(0.24f, 0.40f), Pair(0.50f, 0.36f), Pair(0.76f, 0.40f)  // DEL
        )

        awayStarters.forEachIndexed { idx, p ->
            val slot = awaySlots.getOrElse(idx) { Pair(0.5f, 0.7f) }
            playersList.add(
                Player2D(
                    id = p.id,
                    name = p.name,
                    number = p.number,
                    isHomeTeam = false,
                    positionType = p.position,
                    x = slot.first,
                    y = slot.second,
                    baseAnchorX = slot.first,
                    baseAnchorY = slot.second,
                    overall = p.overall,
                    pace = p.pace,
                    shooting = p.shooting,
                    defending = p.defending,
                    passing = p.passing,
                    aiThought = "Marcando zona"
                )
            )
        }

        val initialBall = Ball2D(
            x = 0.5f,
            y = 0.5f,
            targetX = 0.5f,
            targetY = 0.5f,
            state = BallState.KICKOFF,
            holderId = playersList.firstOrNull { it.isHomeTeam && it.positionType == PositionType.MED }?.id
        )

        _engineState.value = _engineState.value.copy(
            players = playersList,
            ball = initialBall,
            commentaries = listOf(
                MatchCommentary(0, "¡Pitazo inicial! Partido oficial a 30 minutos reglamentarios + tiempo añadido por el árbitro.", isImportant = true)
            )
        )
    }

    fun startSimulation(scope: CoroutineScope) {
        if (_engineState.value.isPlaying || _engineState.value.isFinished) return
        _engineState.value = _engineState.value.copy(isPlaying = true, isHalftime = false)

        simulationJob = scope.launch {
            while (_engineState.value.isPlaying && !_engineState.value.isFinished) {
                delay(130L) // Realistic, smooth videogame broadcast pace
                tick()
            }
        }
    }

    fun resumeSecondHalf(scope: CoroutineScope) {
        if (_engineState.value.currentHalf == 1) {
            _engineState.value = _engineState.value.copy(
                currentHalf = 2,
                minute = half1RegulationMin,
                second = 0,
                isHalftime = false,
                isInStoppageTime = false,
                stoppageBoardNotice = null,
                isPlaying = true,
                commentaries = listOf(
                    MatchCommentary(15, "¡Rueda el balón para el Segundo Tiempo! Los últimos 15 minutos decisivos.", isImportant = true)
                ) + _engineState.value.commentaries
            )
            resetKickoff(_engineState.value.players, _engineState.value.ball)
            startSimulation(scope)
        }
    }

    fun pauseSimulation() {
        _engineState.value = _engineState.value.copy(isPlaying = false)
        simulationJob?.cancel()
    }

    fun setSpeedMultiplier(mult: Int) {
        _engineState.value = _engineState.value.copy(speedMultiplier = mult)
    }

    fun skipToEnd() {
        // Fast-simulate until match conclusion
        while (!_engineState.value.isFinished) {
            tickFast()
        }
        finishMatch()
    }

    fun setInGameMentality(newMentality: Mentality) {
        mentality = newMentality
        val comm = MatchCommentary(
            minute = _engineState.value.minute,
            text = "Orden del DT: Cambio táctico a ${newMentality.displayName}."
        )
        _engineState.value = _engineState.value.copy(
            commentaries = listOf(comm) + _engineState.value.commentaries
        )
    }

    private fun tick() {
        val currentState = _engineState.value
        if (currentState.isFinished || currentState.isHalftime) return

        // Time progression: 5 in-game seconds per tick
        var sec = currentState.second + 5
        var min = currentState.minute
        if (sec >= 60) {
            sec = 0
            min += 1
        }

        var isHalf = currentState.isHalftime
        var isFin = currentState.isFinished
        var isStop = currentState.isInStoppageTime
        var stopNotice = currentState.stoppageBoardNotice
        var addedMin = currentState.addedStoppageMinutes
        val newCommentaries = currentState.commentaries.toMutableList()

        // 1ST HALF: 0 to 15 min + stoppage
        if (currentState.currentHalf == 1) {
            if (min >= half1RegulationMin && !isStop) {
                isStop = true
                addedMin = half1Stoppage
                stopNotice = "⏱️ TABLILLA 4° ÁRBITRO: +$half1Stoppage MIN AÑADIDOS"
                newCommentaries.add(0, MatchCommentary(half1RegulationMin, "El cuarto árbitro añade $half1Stoppage minutos de descuento al primer tiempo.", isImportant = true))
            }
            if (min >= half1RegulationMin + half1Stoppage) {
                // Halftime!
                isHalf = true
                simulationJob?.cancel()
                newCommentaries.add(0, MatchCommentary(half1RegulationMin, "--- ENTRETIEMPO (15' + $half1Stoppage') --- Descanso reglamentario. Los equipos preparan ajustes.", isImportant = true))
                _engineState.value = currentState.copy(
                    minute = half1RegulationMin,
                    second = 0,
                    isHalftime = true,
                    isPlaying = false,
                    isInStoppageTime = false,
                    stoppageBoardNotice = null,
                    commentaries = newCommentaries
                )
                return
            }
        } else {
            // 2ND HALF: 15 to 30 min + stoppage
            if (min >= half2RegulationMin && !isStop) {
                isStop = true
                addedMin = half2Stoppage
                stopNotice = "⏱️ TABLILLA 4° ÁRBITRO: +$half2Stoppage MIN AÑADIDOS"
                newCommentaries.add(0, MatchCommentary(half2RegulationMin, "¡TIEMPO DE DESCUENTO! Se jugarán +$half2Stoppage minutos adicionales señalados por el colegiado.", isImportant = true))
            }
            if (min >= half2RegulationMin + half2Stoppage) {
                finishMatch()
                return
            }
        }

        val ball = currentState.ball
        val players = currentState.players.map { it.copy() }
        var homeScore = currentState.homeScore
        var awayScore = currentState.awayScore
        val homeScorers = currentState.homeScorers.toMutableList()
        val awayScorers = currentState.awayScorers.toMutableList()
        var lastAction = currentState.lastActionDescription
        var bannerEvent: String? = currentState.bannerEventTitle
        var celebrationCountdown = currentState.celebrationCountdown
        var stats = currentState.stats

        // Handle Celebration Countdown
        if (celebrationCountdown > 0) {
            celebrationCountdown--
            if (celebrationCountdown == 0) {
                bannerEvent = null
                resetKickoff(players, ball)
                lastAction = "Saque de centro tras el gol."
            }
            _engineState.value = currentState.copy(
                minute = min,
                second = sec,
                players = players,
                ball = ball,
                celebrationCountdown = celebrationCountdown,
                bannerEventTitle = bannerEvent,
                lastActionDescription = lastAction,
                isInStoppageTime = isStop,
                stoppageBoardNotice = stopNotice,
                addedStoppageMinutes = addedMin
            )
            return
        }

        // Ball movement interpolation
        ball.x += (ball.targetX - ball.x) * ball.speed
        ball.y += (ball.targetY - ball.y) * ball.speed

        val currentHolder = players.firstOrNull { it.id == ball.holderId }
        val isHomeInPossession = currentHolder?.isHomeTeam ?: true

        // Move players according to tactical mentality & ball position
        val mentalityYOffset = when (mentality) {
            Mentality.ALL_OUT_ATTACK -> 0.12f
            Mentality.OFFENSIVE -> 0.06f
            Mentality.BALANCED -> 0.0f
            Mentality.DEFENSIVE -> -0.06f
            Mentality.VERY_DEFENSIVE -> -0.12f
        }

        players.forEach { p ->
            val isBallHolder = p.id == ball.holderId
            val isTeammateWithBall = currentHolder != null && currentHolder.isHomeTeam == p.isHomeTeam
            val baseAnchorY = (p.baseAnchorY + if (p.isHomeTeam) mentalityYOffset else -mentalityYOffset).coerceIn(0.06f, 0.94f)

            if (isBallHolder) {
                p.x = ball.x
                p.y = ball.y
                p.aiThought = when (ball.state) {
                    BallState.SHOOTING -> "¡Armando disparo al arco!"
                    BallState.PENALTY_KICK -> "Frente al punto penal"
                    else -> "Buscando línea de pase o tiro"
                }
            } else if (isTeammateWithBall) {
                when (p.positionType) {
                    PositionType.DEL -> {
                        val runForward = if (p.isHomeTeam) 0.18f else -0.18f
                        val targetY = (baseAnchorY + runForward).coerceIn(0.12f, 0.88f)
                        val targetX = p.baseAnchorX + (sin(min * 0.4f + p.number) * 0.12f).toFloat()
                        p.x += (targetX - p.x) * 0.18f
                        p.y += (targetY - p.y) * 0.18f
                        p.aiThought = "Desmarque al hueco ofensivo"
                    }
                    PositionType.MED -> {
                        val targetX = (p.baseAnchorX * 0.6f + ball.x * 0.4f)
                        val targetY = (baseAnchorY * 0.6f + ball.y * 0.4f)
                        p.x += (targetX - p.x) * 0.14f
                        p.y += (targetY - p.y) * 0.14f
                        p.aiThought = "Pase de apoyo y distribución"
                    }
                    PositionType.DEF -> {
                        val targetY = baseAnchorY + (if (p.isHomeTeam) 0.06f else -0.06f)
                        p.y += (targetY - p.y) * 0.10f
                        p.aiThought = "Línea de cobertura segura"
                    }
                    PositionType.POR -> {
                        p.x += (0.5f + (ball.x - 0.5f) * 0.2f - p.x) * 0.1f
                        p.aiThought = "Atento en portería"
                    }
                }
            } else {
                val distToBall = sqrt((p.x - ball.x).pow(2) + (p.y - ball.y).pow(2))
                if (distToBall < 0.18f && p.positionType != PositionType.POR) {
                    p.x += (ball.x - p.x) * 0.22f
                    p.y += (ball.y - p.y) * 0.22f
                    p.aiThought = "Presión al balón"
                } else {
                    val targetX = p.baseAnchorX + (sin(min * 0.3f + p.number) * 0.04f).toFloat()
                    val targetY = baseAnchorY + (cos(min * 0.3f + p.number) * 0.03f).toFloat()
                    p.x += (targetX - p.x) * 0.12f
                    p.y += (targetY - p.y) * 0.12f
                    p.aiThought = "Bloque defensivo táctico"
                }
            }
        }

        // Gameplay Machine
        when (ball.state) {
            BallState.KICKOFF -> {
                ball.x = 0.5f
                ball.y = 0.5f
                bannerEvent = null
                val mid = players.firstOrNull { it.isHomeTeam == (homeScore <= awayScore) && it.positionType == PositionType.MED && !it.isRedCarded }
                if (mid != null) {
                    ball.targetX = mid.x
                    ball.targetY = mid.y
                    ball.holderId = mid.id
                    ball.state = BallState.CONTROLLED
                    lastAction = "Balón controlado en el centro del campo por ${mid.name}."
                }
            }

            BallState.GOAL_CELEBRATION -> {}

            BallState.PENALTY_KICK -> {
                val taker = players.firstOrNull { it.id == ball.holderId }
                val attackingHome = taker?.isHomeTeam ?: true
                val gk = players.firstOrNull { it.isHomeTeam != attackingHome && it.positionType == PositionType.POR && !it.isRedCarded }

                val goalY = if (attackingHome) 0.94f else 0.06f
                val shotPlacement = 0.44f + (random.nextFloat() * 0.12f)
                ball.targetX = shotPlacement
                ball.targetY = goalY
                ball.speed = 0.08f

                val gkDive = 0.44f + (random.nextFloat() * 0.12f)
                gk?.x = gkDive

                val takerScoreRating = (taker?.shooting ?: 80) + random.nextInt(25)
                val gkSaveRating = (gk?.overall ?: 78) + random.nextInt(30)
                val isSaved = abs(shotPlacement - gkDive) < 0.03f && gkSaveRating > takerScoreRating

                if (!isSaved) {
                    if (attackingHome) {
                        homeScore++
                        homeScorers.add("${taker?.name ?: "Penalti"} ($min' PEN)")
                        lastAction = "¡GOOOL DE PENALTI! ${taker?.name} fusila las redes."
                        newCommentaries.add(0, MatchCommentary(min, "¡GOOOL DE PENALTI de ${homeClub.name}! ${taker?.name} anota con categoría.", isGoal = true, isPenalty = true, isImportant = true))
                    } else {
                        awayScore++
                        awayScorers.add("${taker?.name ?: "Penalti"} ($min' PEN)")
                        lastAction = "¡GOOOL DE PENALTI! ${taker?.name} anota para ${awayClub.name}."
                        newCommentaries.add(0, MatchCommentary(min, "¡GOOOL DE PENALTI de ${awayClub.name}! ${taker?.name} engaña al guardameta.", isGoal = true, isPenalty = true, isImportant = true))
                    }
                    bannerEvent = "¡GOOOL DE PENALTI!"
                    ball.state = BallState.GOAL_CELEBRATION
                    celebrationCountdown = 6
                } else {
                    lastAction = "¡PARADÓN DEL PORTERO! ${gk?.name ?: "El arquero"} detiene el penalti magistralmente."
                    newCommentaries.add(0, MatchCommentary(min, "¡INCREÍBLE PARADÓN! ${gk?.name} adivina el penal y evita el gol.", isPenalty = true, isImportant = true))
                    bannerEvent = "¡PENALTI PARADO!"
                    ball.holderId = gk?.id
                    ball.state = BallState.CONTROLLED
                }
            }

            BallState.FOUL_FREE_KICK -> {
                ball.state = BallState.CONTROLLED
                bannerEvent = null
                lastAction = "Reanudación del juego tras tiro libre."
            }

            BallState.PASSING, BallState.CONTROLLED -> {
                if (currentHolder == null) {
                    val nearest = players.filter { !it.isRedCarded }.minByOrNull {
                        sqrt((it.x - ball.x).pow(2) + (it.y - ball.y).pow(2))
                    }
                    if (nearest != null) {
                        ball.holderId = nearest.id
                        ball.targetX = nearest.x
                        ball.targetY = nearest.y
                        ball.state = BallState.CONTROLLED
                    }
                    return
                }

                val currPoss = stats.homePossession
                val targetPoss = if (isHomeInPossession) (currPoss + 1).coerceAtMost(72) else (currPoss - 1).coerceAtLeast(28)
                stats = stats.copy(homePossession = targetPoss, awayPossession = 100 - targetPoss)

                // Decision to Shoot, Pass, or Dribble
                val attackingGoalY = if (isHomeInPossession) 0.92f else 0.08f
                val distToOpponentGoal = abs(currentHolder.y - attackingGoalY)

                val shouldShoot = distToOpponentGoal < 0.28f && (currentHolder.positionType == PositionType.DEL || currentHolder.positionType == PositionType.MED) && random.nextFloat() < 0.40f

                if (shouldShoot) {
                    val gk = players.firstOrNull { it.isHomeTeam != isHomeInPossession && it.positionType == PositionType.POR && !it.isRedCarded }
                    val goalTargetX = 0.44f + (random.nextFloat() * 0.12f)
                    ball.targetX = goalTargetX
                    ball.targetY = attackingGoalY
                    ball.speed = 0.07f
                    ball.state = BallState.SHOOTING
                    ball.holderId = null

                    if (isHomeInPossession) stats = stats.copy(homeShots = stats.homeShots + 1)
                    else stats = stats.copy(awayShots = stats.awayShots + 1)

                    val shotPower = currentHolder.shooting + random.nextInt(30)
                    val gkReflex = (gk?.defending ?: 75) + random.nextInt(32)

                    if (shotPower > gkReflex + 8) {
                        if (isHomeInPossession) {
                            homeScore++
                            homeScorers.add("${currentHolder.name} ($min')")
                            lastAction = "¡GOOOLAZO DE ${homeClub.name}! ${currentHolder.name} clava el balón en la escuadra."
                            newCommentaries.add(0, MatchCommentary(min, "¡GOOOOL! ${currentHolder.name} saca un disparo imparable. Explota el estadio.", isGoal = true, isImportant = true))
                        } else {
                            awayScore++
                            awayScorers.add("${currentHolder.name} ($min')")
                            lastAction = "¡Gol de ${awayClub.name}! ${currentHolder.name} finaliza una gran jugada."
                            newCommentaries.add(0, MatchCommentary(min, "¡GOL de ${awayClub.name}! ${currentHolder.name} bate al guardameta.", isGoal = true, isImportant = true))
                        }
                        bannerEvent = "¡GOOOL!"
                        ball.state = BallState.GOAL_CELEBRATION
                        celebrationCountdown = 6
                        currentHolder.isCelebrating = true
                    } else if (abs(shotPower - gkReflex) <= 8) {
                        if (isHomeInPossession) stats = stats.copy(homeShotsOnTarget = stats.homeShotsOnTarget + 1)
                        else stats = stats.copy(awayShotsOnTarget = stats.awayShotsOnTarget + 1)
                        lastAction = "¡Gran intervención de ${gk?.name ?: "el arquero"} enviando al córner!"
                        newCommentaries.add(0, MatchCommentary(min, "¡Disparo peligroso de ${currentHolder.name}! Desvía ${gk?.name ?: "el portero"}."))
                        ball.state = BallState.CONTROLLED
                        ball.holderId = gk?.id
                    } else {
                        lastAction = "Disparo desviado por encima del larguero de ${currentHolder.name}."
                        newCommentaries.add(0, MatchCommentary(min, "Remate de ${currentHolder.name} que se marcha fuera de la portería."))
                        ball.state = BallState.CONTROLLED
                        ball.holderId = gk?.id
                    }
                } else {
                    // Pass to teammate
                    val teammates = players.filter { it.isHomeTeam == isHomeInPossession && it.id != currentHolder.id && !it.isRedCarded }
                    val forwardTeammates = teammates.filter {
                        if (isHomeInPossession) it.y > currentHolder.y else it.y < currentHolder.y
                    }
                    val passTarget = if (forwardTeammates.isNotEmpty() && random.nextFloat() < 0.65f) {
                        forwardTeammates.random(random)
                    } else {
                        teammates.randomOrNull(random)
                    }

                    if (passTarget != null) {
                        val passAcc = currentHolder.passing + random.nextInt(25)
                        val interceptor = players.filter { it.isHomeTeam != isHomeInPossession && !it.isRedCarded }.minByOrNull {
                            sqrt((it.x - passTarget.x).pow(2) + (it.y - passTarget.y).pow(2))
                        }
                        val interceptDef = (interceptor?.defending ?: 70) + random.nextInt(30)

                        if (interceptDef > passAcc + 14 && interceptor != null) {
                            ball.holderId = interceptor.id
                            ball.targetX = interceptor.x
                            ball.targetY = interceptor.y
                            ball.speed = 0.04f
                            ball.state = BallState.CONTROLLED
                            lastAction = "Intercepción limpia de ${interceptor.name} recuperando la posesión."
                        } else {
                            ball.holderId = passTarget.id
                            ball.targetX = passTarget.x
                            ball.targetY = passTarget.y
                            ball.speed = 0.05f
                            ball.state = BallState.PASSING
                            lastAction = "Pase preciso de ${currentHolder.name} hacia ${passTarget.name}."
                        }
                    }
                }
            }

            BallState.SHOOTING -> {
                ball.state = BallState.CONTROLLED
            }
        }

        _engineState.value = currentState.copy(
            minute = min,
            second = sec,
            homeScore = homeScore,
            awayScore = awayScore,
            ball = ball,
            players = players,
            commentaries = newCommentaries.take(30),
            stats = stats,
            homeScorers = homeScorers,
            awayScorers = awayScorers,
            lastActionDescription = lastAction,
            bannerEventTitle = bannerEvent,
            celebrationCountdown = celebrationCountdown,
            isInStoppageTime = isStop,
            stoppageBoardNotice = stopNotice,
            addedStoppageMinutes = addedMin
        )
    }

    private fun tickFast() {
        val s = _engineState.value
        var m = s.minute + 1
        var hs = s.homeScore
        var ascore = s.awayScore
        val hScorers = s.homeScorers.toMutableList()
        val aScorers = s.awayScorers.toMutableList()

        if (random.nextFloat() < 0.12f) {
            val isHome = random.nextBoolean()
            if (isHome) {
                hs++
                hScorers.add("Gol ($m')")
            } else {
                ascore++
                aScorers.add("Gol ($m')")
            }
        }
        _engineState.value = s.copy(
            minute = m.coerceAtMost(34),
            second = 0,
            homeScore = hs,
            awayScore = ascore,
            homeScorers = hScorers,
            awayScorers = aScorers
        )
    }

    private fun resetKickoff(players: List<Player2D>, ball: Ball2D) {
        players.forEach { p ->
            p.x = p.baseAnchorX
            p.y = p.baseAnchorY
            p.isCelebrating = false
            p.aiThought = "Reanudando saque de centro"
        }
        ball.x = 0.5f
        ball.y = 0.5f
        ball.targetX = 0.5f
        ball.targetY = 0.5f
        ball.state = BallState.KICKOFF
        ball.holderId = players.firstOrNull { it.isHomeTeam && it.positionType == PositionType.MED && !it.isRedCarded }?.id
    }

    private fun finishMatch() {
        _engineState.value = _engineState.value.copy(
            isPlaying = false,
            isFinished = true,
            minute = 30 + half2Stoppage,
            second = 0,
            isInStoppageTime = true,
            stoppageBoardNotice = null,
            commentaries = listOf(
                MatchCommentary(30, "¡PITAZO FINAL! El colegiado señala la conclusión del encuentro (30' + $half2Stoppage').", isImportant = true)
            ) + _engineState.value.commentaries
        )
        simulationJob?.cancel()
    }
}
