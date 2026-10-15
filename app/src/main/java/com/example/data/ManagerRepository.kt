package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ManagerRepository {

    private val _clubs = MutableStateFlow<List<Club>>(InitialData.clubs)
    val clubs: StateFlow<List<Club>> = _clubs.asStateFlow()

    private val _userSquad = MutableStateFlow<List<Player>>(InitialData.nervaPlayers)
    val userSquad: StateFlow<List<Player>> = _userSquad.asStateFlow()

    private val _marketPlayers = MutableStateFlow<List<Player>>(InitialData.marketPool)
    val marketPlayers: StateFlow<List<Player>> = _marketPlayers.asStateFlow()

    private val _tactic = MutableStateFlow(
        TacticConfig(
            formation = Formation.F_433,
            playStyle = PlayStyle.TIKI_TAKA,
            mentality = Mentality.BALANCED,
            pressing = PressingIntensity.HIGH,
            passingDirectness = 0.35f,
            captainPlayerId = "p_n6", // Lucas Kova
            penaltyTakerId = "p_n10", // Gabriel Nova
            freeKickTakerId = "p_n8" // Enzo Navarro
        )
    )
    val tactic: StateFlow<TacticConfig> = _tactic.asStateFlow()

    private val _matches = MutableStateFlow<List<Match>>(InitialData.initialMatches)
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _forumPosts = MutableStateFlow<List<ForumPost>>(InitialData.initialForumPosts)
    val forumPosts: StateFlow<List<ForumPost>> = _forumPosts.asStateFlow()

    private val _incomingOffers = MutableStateFlow<List<TransferOffer>>(InitialData.initialIncomingOffers)
    val incomingOffers: StateFlow<List<TransferOffer>> = _incomingOffers.asStateFlow()

    private val _sponsors = MutableStateFlow<List<Sponsor>>(InitialData.initialSponsors)
    val sponsors: StateFlow<List<Sponsor>> = _sponsors.asStateFlow()

    // Active simulated match
    private val _activeMatch = MutableStateFlow<Match?>(null)
    val activeMatch: StateFlow<Match?> = _activeMatch.asStateFlow()

    // --- LIGA GAMMA NERVA & 12 DIVISIONS ---
    private val _leagues = MutableStateFlow<List<League>>(listOf(GammaLeagueData.gammaLeague))
    val leagues: StateFlow<List<League>> = _leagues.asStateFlow()

    private val _divisions = MutableStateFlow<Map<Int, LeagueDivision>>(
        GammaLeagueData.generate12Divisions(_clubs.value.first { it.isUserClub })
    )
    val divisions: StateFlow<Map<Int, LeagueDivision>> = _divisions.asStateFlow()

    private val _selectedDivisionNumber = MutableStateFlow(12)
    val selectedDivisionNumber: StateFlow<Int> = _selectedDivisionNumber.asStateFlow()

    // --- SCHEDULED MATCHES (5-HOUR INTERVALS: [5:00AM, 10:00AM, 3:00PM, 8:00PM]) ---
    private val _scheduledSlots = MutableStateFlow<List<ScheduledMatchSlot>>(GammaLeagueData.defaultScheduledSlots)
    val scheduledSlots: StateFlow<List<ScheduledMatchSlot>> = _scheduledSlots.asStateFlow()

    // --- THEME & BACKGROUND CUSTOMIZATION ---
    private val _backgroundTheme = MutableStateFlow(AppBackgroundTheme.ESTADIO_NOCTURNO)
    val backgroundTheme: StateFlow<AppBackgroundTheme> = _backgroundTheme.asStateFlow()

    // --- CUSTOM MUSIC & AUDIO ---
    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying: StateFlow<Boolean> = _isMusicPlaying.asStateFlow()

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex.asStateFlow()

    private val _musicVolume = MutableStateFlow(0.75f)
    val musicVolume: StateFlow<Float> = _musicVolume.asStateFlow()

    private val _customAudioUrl = MutableStateFlow("")
    val customAudioUrl: StateFlow<String> = _customAudioUrl.asStateFlow()

    val userClub: Club
        get() = _clubs.value.first { it.isUserClub }

    // --- TACTICS & SQUAD MANAGEMENT ---
    fun updateFormation(newFormation: Formation) {
        _tactic.update { it.copy(formation = newFormation) }
        // Ensure starting XI alignment with slots
        val slots = TacticConfig.getSlotsForFormation(newFormation)
        val starters = _userSquad.value.filter { it.isStarter }.toMutableList()
        val bench = _userSquad.value.filter { !it.isStarter }.toMutableList()

        // Assign pitch slots up to 11
        val updatedSquad = mutableListOf<Player>()
        for (i in 0 until 11) {
            val player = if (i < starters.size) starters[i] else bench.removeFirstOrNull()
            if (player != null) {
                updatedSquad.add(player.copy(isStarter = true, lineupPosition = i))
            }
        }
        bench.forEach {
            updatedSquad.add(it.copy(isStarter = false, lineupPosition = -1))
        }
        _userSquad.value = updatedSquad
    }

    fun updatePlayStyle(style: PlayStyle) {
        _tactic.update { it.copy(playStyle = style) }
    }

    fun updateMentality(mentality: Mentality) {
        _tactic.update { it.copy(mentality = mentality) }
    }

    fun updatePressing(pressing: PressingIntensity) {
        _tactic.update { it.copy(pressing = pressing) }
    }

    fun swapPlayers(starterId: String, benchId: String) {
        val currentSquad = _userSquad.value.toMutableList()
        val p1Index = currentSquad.indexOfFirst { it.id == starterId }
        val p2Index = currentSquad.indexOfFirst { it.id == benchId }

        if (p1Index != -1 && p2Index != -1) {
            val p1 = currentSquad[p1Index]
            val p2 = currentSquad[p2Index]

            currentSquad[p1Index] = p1.copy(
                isStarter = p2.isStarter,
                lineupPosition = p2.lineupPosition
            )
            currentSquad[p2Index] = p2.copy(
                isStarter = p1.isStarter,
                lineupPosition = p1.lineupPosition
            )
            _userSquad.value = currentSquad
        }
    }

    fun setCaptain(playerId: String) {
        _tactic.update { it.copy(captainPlayerId = playerId) }
    }

    fun setPenaltyTaker(playerId: String) {
        _tactic.update { it.copy(penaltyTakerId = playerId) }
    }

    fun updatePlayerNationality(playerId: String, newNationality: String) {
        _userSquad.update { squad ->
            squad.map { if (it.id == playerId) it.copy(nationality = newNationality) else it }
        }
        _marketPlayers.update { pool ->
            pool.map { if (it.id == playerId) it.copy(nationality = newNationality) else it }
        }
    }

    fun claimSponsorRevenue(amount: Long, sponsorName: String) {
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) it.copy(budget = it.budget + amount) else it
            }
        }
        val post = ForumPost(
            id = "fp_${java.util.UUID.randomUUID()}",
            authorName = "Finanzas del Club",
            authorClubId = "official",
            authorRole = "NERVA FC Oficial",
            title = "Acuerdo de Patrocinio: $sponsorName",
            content = "NERVA FC ha ingresado $ " + String.format("%,d", amount).replace(",", ".") + " gracias al contrato comercial con $sponsorName.",
            category = "Fichajes",
            timeAgo = "Ahora",
            likesCount = 28
        )
        _forumPosts.update { listOf(post) + it }
    }

    // --- TRANSFER MARKET ---
    fun buyPlayer(player: Player, offerPrice: Long): Boolean {
        val userClub = userClub
        if (userClub.budget < offerPrice) return false

        // Transfer successful
        _clubs.update { list ->
            list.map { club ->
                when {
                    club.id == userClub.id -> club.copy(budget = club.budget - offerPrice)
                    club.id == player.clubId -> club.copy(budget = club.budget + offerPrice)
                    else -> club
                }
            }
        }

        // Move player to user squad
        val transferredPlayer = player.copy(
            clubId = userClub.id,
            isStarter = false,
            lineupPosition = -1,
            isTransferListed = false
        )

        _marketPlayers.update { it.filter { p -> p.id != player.id } }
        _userSquad.update { it + transferredPlayer }

        // Add news in forum
        val post = ForumPost(
            id = "fp_${UUID.randomUUID()}",
            authorName = "Mercado Central",
            authorClubId = "official",
            authorRole = "Fichajes Oficiales",
            title = "¡BOMBAZO! ${transferredPlayer.name} ficha por NERVA CF",
            content = "NERVA CF ha cerrado la contratación de ${transferredPlayer.name} por una suma de ${transferredPlayer.formattedValue}. Gran refuerzo para el club.",
            category = "Fichajes",
            timeAgo = "Ahora",
            likesCount = 42,
            replies = listOf(
                ForumReply("fr_${UUID.randomUUID()}", "El Profe Díaz", "DT Chilenos FC", "Gran fichaje del míster de NERVA, habrá que marcarlo de cerca.", "Ahora", 4)
            )
        )
        _forumPosts.update { listOf(post) + it }
        return true
    }

    fun toggleTransferList(playerId: String) {
        _userSquad.update { squad ->
            squad.map {
                if (it.id == playerId) it.copy(isTransferListed = !it.isTransferListed) else it
            }
        }
    }

    fun acceptIncomingOffer(offerId: String) {
        val offer = _incomingOffers.value.find { it.id == offerId } ?: return
        val player = _userSquad.value.find { it.id == offer.playerId } ?: return

        // Credit money to user club
        _clubs.update { list ->
            list.map { club ->
                if (club.isUserClub) club.copy(budget = club.budget + offer.amount) else club
            }
        }

        // Remove player from user squad
        _userSquad.update { it.filter { p -> p.id != player.id } }

        // Remove offer
        _incomingOffers.update { it.filter { o -> o.id != offerId } }

        // Forum post
        val post = ForumPost(
            id = "fp_${UUID.randomUUID()}",
            authorName = offer.fromClubName,
            authorClubId = offer.fromClubId,
            authorRole = "Comunicado Oficial",
            title = "Traspaso confirmado: ${player.name} pasa a ${offer.fromClubName}",
            content = "Acuerdo total entre clubes por ${offer.formattedAmount}. Agradecemos a NERVA CF por la seriedad en las negociaciones.",
            category = "Fichajes",
            timeAgo = "Ahora",
            likesCount = 25
        )
        _forumPosts.update { listOf(post) + it }
    }

    fun rejectIncomingOffer(offerId: String) {
        _incomingOffers.update { it.filter { o -> o.id != offerId } }
    }

    // --- FORUM ACTIONS ---
    fun createPost(title: String, content: String, category: String) {
        val newPost = ForumPost(
            id = "fp_${UUID.randomUUID()}",
            authorName = "DT Nerva (Tú)",
            authorClubId = "c_nerva",
            authorRole = "Director Técnico de NERVA CF",
            title = title,
            content = content,
            category = category,
            timeAgo = "Ahora",
            likesCount = 1,
            isLikedByUser = true,
            replies = listOf(
                ForumReply(
                    id = "fr_${UUID.randomUUID()}",
                    authorName = "El Profe Díaz",
                    authorRole = "DT Chilenos FC",
                    content = "Interesante punto, colega. Siempre es un gusto debatir de fútbol con técnicos comprometidos.",
                    timeAgo = "Ahora",
                    likesCount = 2
                )
            )
        )
        _forumPosts.update { listOf(newPost) + it }
    }

    fun addReply(postId: String, text: String) {
        val reply = ForumReply(
            id = "fr_${UUID.randomUUID()}",
            authorName = "DT Nerva (Tú)",
            authorRole = "Director Técnico de NERVA CF",
            content = text,
            timeAgo = "Ahora",
            likesCount = 0
        )
        _forumPosts.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    post.copy(replies = post.replies + reply)
                } else post
            }
        }
    }

    fun toggleLike(postId: String) {
        _forumPosts.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    val wasLiked = post.isLikedByUser
                    post.copy(
                        isLikedByUser = !wasLiked,
                        likesCount = if (wasLiked) post.likesCount - 1 else post.likesCount + 1
                    )
                } else post
            }
        }
    }

    // --- MATCH SIMULATION & LEAGUE UPDATES ---
    fun setActiveMatch(match: Match?) {
        _activeMatch.value = match
    }

    fun recordMatchResult(
        matchId: String,
        homeScore: Int,
        awayScore: Int,
        homeScorers: List<String>,
        awayScorers: List<String>,
        stats: MatchStats,
        commentaries: List<MatchCommentary>
    ) {
        val match = _matches.value.find { it.id == matchId } ?: return

        // Update match in fixtures
        val updatedMatch = match.copy(
            homeScore = homeScore,
            awayScore = awayScore,
            isPlayed = true,
            isSimulating = false,
            homeScorers = homeScorers,
            awayScorers = awayScorers,
            stats = stats,
            commentaries = commentaries
        )

        _matches.update { list ->
            list.map { if (it.id == matchId) updatedMatch else it }
        }

        // Recalculate league standings
        val homeWon = homeScore > awayScore
        val draw = homeScore == awayScore
        val awayWon = awayScore > homeScore

        _clubs.update { clubsList ->
            clubsList.map { club ->
                when (club.id) {
                    match.homeClubId -> {
                        val newPlayed = club.played + 1
                        val newWon = club.won + if (homeWon) 1 else 0
                        val newDrawn = club.drawn + if (draw) 1 else 0
                        val newLost = club.lost + if (awayWon) 1 else 0
                        val newPoints = club.points + (if (homeWon) 3 else if (draw) 1 else 0)
                        val formResult = if (homeWon) "W" else if (draw) "D" else "L"
                        club.copy(
                            played = newPlayed,
                            won = newWon,
                            drawn = newDrawn,
                            lost = newLost,
                            goalsFor = club.goalsFor + homeScore,
                            goalsAgainst = club.goalsAgainst + awayScore,
                            points = newPoints,
                            recentForm = listOf(formResult) + club.recentForm.take(4)
                        )
                    }
                    match.awayClubId -> {
                        val newPlayed = club.played + 1
                        val newWon = club.won + if (awayWon) 1 else 0
                        val newDrawn = club.drawn + if (draw) 1 else 0
                        val newLost = club.lost + if (homeWon) 1 else 0
                        val newPoints = club.points + (if (awayWon) 3 else if (draw) 1 else 0)
                        val formResult = if (awayWon) "W" else if (draw) "D" else "L"
                        club.copy(
                            played = newPlayed,
                            won = newWon,
                            drawn = newDrawn,
                            lost = newLost,
                            goalsFor = club.goalsFor + awayScore,
                            goalsAgainst = club.goalsAgainst + homeScore,
                            points = newPoints,
                            recentForm = listOf(formResult) + club.recentForm.take(4)
                        )
                    }
                    else -> club
                }
            }
        }

        // Reward user club budget if won
        if ((match.homeClubId == userClub.id && homeWon) || (match.awayClubId == userClub.id && awayWon)) {
            _clubs.update { list ->
                list.map {
                    if (it.isUserClub) it.copy(budget = it.budget + 2_500_000L) else it
                }
            }
        } else if (draw) {
            _clubs.update { list ->
                list.map {
                    if (it.isUserClub) it.copy(budget = it.budget + 1_000_000L) else it
                }
            }
        }

        // Post result in forum automatically
        val homeClub = _clubs.value.firstOrNull { it.id == match.homeClubId }
        val awayClub = _clubs.value.firstOrNull { it.id == match.awayClubId }
        if (homeClub != null && awayClub != null) {
            val forumPost = ForumPost(
                id = "fp_${UUID.randomUUID()}",
                authorName = "Transmisión Oficial",
                authorClubId = "official",
                authorRole = "Liga NERVA - Jornada ${match.round}",
                title = "Final: ${homeClub.name} $homeScore - $awayScore ${awayClub.name}",
                content = "Terminó el duelo de alta tensión en el ${homeClub.stadiumName}. Resultado clave para la tabla de posiciones.",
                category = "Partidos",
                timeAgo = "Ahora",
                likesCount = 33,
                replies = listOf(
                    ForumReply(
                        id = "fr_${UUID.randomUUID()}",
                        authorName = if (homeClub.isUserClub) awayClub.managerName else homeClub.managerName,
                        authorRole = "DT rival",
                        content = "Intenso partido. Tuvimos momentos para liquidar pero así es el fútbol.",
                        timeAgo = "Ahora",
                        likesCount = 7
                    )
                )
            )
            _forumPosts.update { listOf(forumPost) + it }
        }
    }

    // --- SPONSORS & FLAGS MANAGEMENT ---
    fun toggleSignSponsor(sponsorId: String): Boolean {
        var signed = false
        _sponsors.update { list ->
            list.map { sp ->
                if (sp.id == sponsorId) {
                    signed = !sp.isSigned
                    sp.copy(isSigned = signed)
                } else sp
            }
        }
        if (signed) {
            // Immediate sign bonus to club budget
            _clubs.update { list ->
                list.map {
                    if (it.isUserClub) it.copy(budget = it.budget + 500_000L) else it
                }
            }
        }
        return signed
    }

    fun updateClubFlag(clubId: String, newFlag: String) {
        _clubs.update { list ->
            list.map {
                if (it.id == clubId) it.copy(flag = newFlag) else it
            }
        }
    }

    fun updatePlayerFlag(playerId: String, newFlag: String) {
        _userSquad.update { list ->
            list.map {
                if (it.id == playerId) it.copy(nationality = newFlag) else it
            }
        }
        _marketPlayers.update { list ->
            list.map {
                if (it.id == playerId) it.copy(nationality = newFlag) else it
            }
        }
    }

    // --- ADMIN CHEATS & OPERATIONS (iamnevulex) ---
    fun addBudget(amount: Long) {
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) it.copy(budget = it.budget + amount) else it
            }
        }
    }

    fun setBudget(amount: Long) {
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) it.copy(budget = amount) else it
            }
        }
    }

    fun boostAllStamina() {
        _userSquad.update { list ->
            list.map {
                it.copy(stamina = 100)
            }
        }
    }

    fun boostAllOverall(bonus: Int = 5) {
        _userSquad.update { list ->
            list.map {
                val newOv = (it.overall + bonus).coerceAtMost(99)
                it.copy(
                    overall = newOv,
                    pace = (it.pace + bonus).coerceAtMost(99),
                    shooting = (it.shooting + bonus).coerceAtMost(99),
                    passing = (it.passing + bonus).coerceAtMost(99),
                    dribbling = (it.dribbling + bonus).coerceAtMost(99),
                    defending = (it.defending + bonus).coerceAtMost(99),
                    physical = (it.physical + bonus).coerceAtMost(99),
                    marketValue = it.marketValue + 5_000_000L
                )
            }
        }
    }

    fun clearDisciplinaryCards() {
        // Disciplinary reset: clear yellow/red cards and boost morale
        _userSquad.update { list ->
            list.map { it.copy(yellowCards = 0, redCards = 0, morale = "Excelente") }
        }
    }

    fun upgradeFacilities() {
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) it.copy(reputation = 99, coins = it.coins + 5000) else it
            }
        }
    }

    fun updateClubReputation(reputation: Int) {
        val repScore = (reputation * 20).coerceIn(40, 99)
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) it.copy(reputation = repScore) else it
            }
        }
    }

    fun updatePlayerStats(playerId: String, newOverall: Int, newMarketValue: Long) {
        _userSquad.update { list ->
            list.map {
                if (it.id == playerId) it.copy(overall = newOverall, marketValue = newMarketValue) else it
            }
        }
    }

    // --- LIGA GAMMA NERVA & DIVISIONS OPERATIONS ---
    fun selectDivision(divisionNumber: Int) {
        _selectedDivisionNumber.value = divisionNumber.coerceIn(1, 12)
    }

    fun createLeague(
        name: String,
        divisionsCount: Int,
        maxClubs: Int,
        promotesFrom: String?,
        icon: String,
        banner: String
    ) {
        val newLeague = League(
            id = "league_" + UUID.randomUUID().toString().take(6),
            name = name,
            divisionsCount = divisionsCount,
            maxClubsPerDivision = maxClubs,
            promotesFromLeagueId = promotesFrom,
            bannerStyle = banner,
            iconEmoji = icon
        )
        _leagues.update { it + newLeague }
    }

    fun updateLeagueBannerAndIcon(leagueId: String, banner: String, icon: String) {
        _leagues.update { list ->
            list.map {
                if (it.id == leagueId) it.copy(bannerStyle = banner, iconEmoji = icon) else it
            }
        }
    }

    // --- SCHEDULED MATCHES (5-HOUR: [5:00AM, 10:00AM, 3:00PM, 8:00PM]) ---
    fun simulateScheduledSlot(slotId: String): String {
        val slot = _scheduledSlots.value.firstOrNull { it.id == slotId } ?: return "Jornada no encontrada"
        val userWon = (1..10).random() > 3
        val homeGoals = if (userWon) (1..4).random() else (0..1).random()
        val awayGoals = if (userWon) (0..1).random() else (2..3).random()
        val scoreStr = "${userClub.name} $homeGoals - $awayGoals Rival FC (${slot.label})"

        _scheduledSlots.update { list ->
            list.map {
                if (it.id == slotId) it.copy(lastSimulatedScore = scoreStr, isCompletedToday = true) else it
            }
        }

        // Award prize money and points to user club in division 12
        if (userWon) {
            addBudget(350_000L)
        }
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) {
                    val pts = if (userWon) it.points + 3 else it.points
                    val won = if (userWon) it.won + 1 else it.won
                    val lost = if (!userWon) it.lost + 1 else it.lost
                    it.copy(
                        played = it.played + 1,
                        won = won,
                        lost = lost,
                        goalsFor = it.goalsFor + homeGoals,
                        goalsAgainst = it.goalsAgainst + awayGoals,
                        points = pts
                    )
                } else it
            }
        }

        // Re-sort division 12
        _divisions.update { map ->
            val div12 = map[12]
            if (div12 != null) {
                val updatedClubs = div12.clubs.map {
                    if (it.isUserClub) {
                        val pts = if (userWon) it.points + 3 else it.points
                        val won = if (userWon) it.won + 1 else it.won
                        val lost = if (!userWon) it.lost + 1 else it.lost
                        it.copy(
                            played = it.played + 1,
                            won = won,
                            lost = lost,
                            goalsFor = it.goalsFor + homeGoals,
                            goalsAgainst = it.goalsAgainst + awayGoals,
                            points = pts
                        )
                    } else it
                }.sortedWith(
                    compareByDescending<Club> { it.points }
                        .thenByDescending { it.goalDifference }
                        .thenByDescending { it.goalsFor }
                )
                map + (12 to div12.copy(clubs = updatedClubs))
            } else map
        }

        return scoreStr
    }

    // --- CREATE PLAYER (ADMIN PANEL) ---
    fun createCustomPlayer(
        name: String,
        position: PositionType,
        roleTag: String,
        nationality: String,
        age: Int,
        overall: Int,
        pace: Int,
        shooting: Int,
        passing: Int,
        dribbling: Int,
        defending: Int,
        physical: Int,
        marketValue: Long,
        destination: String // "SQUAD" or "MARKET"
    ) {
        val newPlayer = Player(
            id = "p_custom_" + UUID.randomUUID().toString().take(6),
            name = name,
            number = (1..99).random(),
            position = position,
            specificPos = roleTag,
            age = age,
            nationality = nationality,
            clubName = if (destination == "SQUAD") userClub.name else "Agente Libre",
            overall = overall,
            pace = pace,
            shooting = shooting,
            passing = passing,
            dribbling = dribbling,
            defending = defending,
            physical = physical,
            stamina = 100,
            morale = "Excelente",
            marketValue = marketValue,
            wage = (marketValue / 500L).coerceAtLeast(15_000L),
            clubId = if (destination == "SQUAD") userClub.id else "",
            isStarter = false,
            lineupPosition = -1
        )

        if (destination == "SQUAD") {
            _userSquad.update { it + newPlayer }
        } else {
            _marketPlayers.update { it + newPlayer }
        }
    }

    // --- ADVANCED CLUB PROFILE EDIT (Lema, Icono/Logo, Nombre, etc.) ---
    fun updateClubProfile(
        name: String,
        shortName: String,
        motto: String,
        managerName: String,
        stadiumName: String,
        primaryColorHex: Long,
        secondaryColorHex: Long,
        badgeShape: String,
        badgeSymbol: String,
        badgeBorderColorHex: Long,
        bannerStyle: String
    ) {
        _clubs.update { list ->
            list.map {
                if (it.isUserClub) {
                    it.copy(
                        name = name,
                        shortName = shortName,
                        motto = motto,
                        managerName = managerName,
                        stadiumName = stadiumName,
                        primaryColorHex = primaryColorHex,
                        secondaryColorHex = secondaryColorHex,
                        badgeShape = badgeShape,
                        badgeSymbol = badgeSymbol,
                        badgeBorderColorHex = badgeBorderColorHex,
                        bannerStyle = bannerStyle
                    )
                } else it
            }
        }
        _divisions.update { map ->
            val div12 = map[12]
            if (div12 != null) {
                val updatedClubs = div12.clubs.map {
                    if (it.isUserClub) {
                        it.copy(
                            name = name,
                            shortName = shortName,
                            motto = motto,
                            managerName = managerName,
                            stadiumName = stadiumName,
                            primaryColorHex = primaryColorHex,
                            secondaryColorHex = secondaryColorHex,
                            badgeShape = badgeShape,
                            badgeSymbol = badgeSymbol,
                            badgeBorderColorHex = badgeBorderColorHex,
                            bannerStyle = bannerStyle
                        )
                    } else it
                }
                map + (12 to div12.copy(clubs = updatedClubs))
            } else map
        }
    }

    // --- BACKGROUND THEME & MUSIC ---
    fun setBackgroundTheme(theme: AppBackgroundTheme) {
        _backgroundTheme.value = theme
    }

    fun toggleMusic() {
        _isMusicPlaying.value = !_isMusicPlaying.value
    }

    fun nextMusicTrack() {
        _currentTrackIndex.value = (_currentTrackIndex.value + 1) % 4
    }

    fun setMusicVolume(vol: Float) {
        _musicVolume.value = vol.coerceIn(0f, 1f)
    }

    fun setCustomAudioUrl(url: String) {
        _customAudioUrl.value = url
    }
}
